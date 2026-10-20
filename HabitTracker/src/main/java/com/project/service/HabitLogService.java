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

import java.security.Principal;
import java.util.List;

@Service
public class HabitLogService {
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;

    public List<HabitLog> findAllHabitLogs(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return habitLogRepository.findByHabitId(id);
    }
    public HabitDto addHabitLog(HabitDto habitDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        Habit habit = habitRepository.findById(habitDto.getId()).get();
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setCompletedDate(habitDto.getDate());
        habitLogRepository.save(habitLog);
        return habitDto;

    }
}
