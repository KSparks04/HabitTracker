package com.project.service;

import com.project.dto.HabitDto;
import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import com.project.repository.HabitLogRepository;
import com.project.repository.HabitRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HabitService {
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;
    public List<HabitDto> getHabitsForUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        List<Habit> habits = habitRepository.findByUserId(user.getId());
        List<HabitDto> habitDtos = new ArrayList<>();
        for(Habit habit : habits){
            HabitDto habitDto = new HabitDto();
            habitDto.setId(habit.getId());
            habitDto.setTitle(habit.getTitle());
            habitDto.setFrequency(habit.getFrequency());
            habitDto.setNotes(habit.getNotes());
            habitDto.setColor(habit.getColor());
            habitDtos.add(habitDto);
        }
        return  habitDtos;
    }
    public HabitDto createHabitForUser(HabitDto habitDto, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        System.out.println(habitDto.toString());
        Habit habit = new Habit();
        habit.setUser(user);
        habit.setTitle(habitDto.getTitle());
        habit.setSaved(true);
        habit.setFrequency(habitDto.getFrequency());
        habit.setIconName(habitDto.getIconName());
        habit.setColor(habitDto.getColor());
        habitRepository.save(habit);
        return  habitDto;
    }
    public HabitDto updateHabitForUser(Long id,HabitDto habitDto, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        Habit habit = habitRepository.findById(habitDto.getId()).get();
        habit.setTitle(habitDto.getTitle());
        habit.setSaved(true);
        habit.setFrequency(habitDto.getFrequency());
        habit.setNotes(habitDto.getNotes());
        habit.setColor(habitDto.getColor());
        habitRepository.save(habit);
        return  habitDto;
    }
    public String deleteHabitForUser(Long id,String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        Habit habit = habitRepository.findById(id).get();
        habitRepository.delete(habit);
        return  "Habit deleted successfully";
    }
    public HabitDto toggleHabitLog(HabitDto habitDto, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        if(habitDto.getLogged() == true){
            List<HabitLog> habitLogs =  habitLogRepository.findByHabitId(habitDto.getId());

            for(HabitLog log: habitLogs){
                if(log.getCompletedDate() == habitDto.getDate()){
                    habitLogRepository.delete(log);
                    habitDto.setLogged(false);
                    break;
                }
            }

            return habitDto;

        }
        Habit habit = habitRepository.findById(habitDto.getId()).get();
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setCompletedDate(habitDto.getDate());
        habitLogRepository.save(habitLog);
        habitDto.setLogged(true);
        return habitDto;

    }

}
