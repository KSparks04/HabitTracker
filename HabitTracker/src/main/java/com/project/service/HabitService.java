package com.project.service;

import com.project.dto.HabitDto;
import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import com.project.repository.HabitLogRepository;
import com.project.repository.HabitRepository;
import com.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HabitService {
    private static final Set<String> ALLOWED_FREQUENCIES = Set.of("DAILY", "WEEKLY", "MONTHLY");
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;

    public List<HabitDto> getHabitsForUser(String email){
        User user = getUserByEmail(email);
        List<Habit> habits = habitRepository.findByUserId(user.getId());
        if (habits.isEmpty()) {
            return List.of();
        }
        List<HabitDto> habitDtos = new ArrayList<>();
        for(Habit habit : habits){
            HabitDto habitDto = new HabitDto();
            habitDto.setId(habit.getId());
            habitDto.setTitle(habit.getTitle());
            habitDto.setFrequency(habit.getFrequency());
            habitDto.setNotes(habit.getNotes());
            habitDto.setColor(habit.getColor());
            habitDto.setIconName(habit.getIconName());
            habitDtos.add(habitDto);
        }
        return  habitDtos;
    }
    public HabitDto createHabitForUser(HabitDto habitDto, String email){
        User user = getUserByEmail(email);
        validateHabitData(habitDto,false);

        Habit habit = new Habit();
        habit.setUser(user);
        habit.setTitle(habitDto.getTitle());
        habit.setSaved(true);
        habit.setFrequency(habitDto.getFrequency());
        habit.setIconName(habitDto.getIconName());
        habit.setColor(habitDto.getColor());
        Habit saved = habitRepository.save(habit);
        habitDto.setId(saved.getId());
        habitDto.setTitle(saved.getTitle());
        habitDto.setFrequency(saved.getFrequency());
        return  habitDto;

    }
    public HabitDto updateHabitForUser(HabitDto habitDto, String email){
        User user = getUserByEmail(email);
        validateHabitData(habitDto,true);
        Habit habit = getOwnedHabit(habitDto.getId(), user.getId());
        habit.setTitle(habitDto.getTitle());
        habit.setSaved(true);
        habit.setFrequency(habitDto.getFrequency());
        habit.setNotes(habitDto.getNotes());
        habit.setColor(habitDto.getColor());
        Habit saved = habitRepository.save(habit);
        habitDto.setId(saved.getId());
        habitDto.setTitle(saved.getTitle());
        habitDto.setFrequency(saved.getFrequency());
        return habitDto;
    }
    @Transactional
    public String deleteHabitForUser(Long id,String email){
        User user = getUserByEmail(email);
        if (id == null) {
            throw new IllegalArgumentException("Habit id must not be null");
        }
        Habit habit = getOwnedHabit(id, user.getId());
        habitLogRepository.deleteByHabitId(habit.getId());
        habitRepository.delete(habit);

        return  "Habit deleted successfully";
    }
    @Transactional
    public HabitDto toggleHabitLog(HabitDto habitDto, String email){
        User user = getUserByEmail(email);
        if (habitDto == null || habitDto.getId() == null) {
            throw new IllegalArgumentException("Habit id is required to toggle log");
        }

        Habit habit = getOwnedHabit(habitDto.getId(), user.getId());
        LocalDateTime targetDate = habitDto.getDate() != null ? habitDto.getDate() : LocalDateTime.now();
        boolean currentlyLogged = Boolean.TRUE.equals(habitDto.getLogged());
        if(currentlyLogged){
            List<HabitLog> habitLogs =  habitLogRepository.findByHabitId(habitDto.getId());

            for(HabitLog log: habitLogs){
                if(targetDate.toLocalDate().equals(log.getCompletedDate().toLocalDate())){
                    habitLogRepository.delete(log);
                    habitDto.setLogged(false);
                    habitDto.setDate(targetDate);
                    break;
                }
            }
            habitDto.setLogged(false);

            return habitDto;

        }

        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setCompletedDate(targetDate);
        habitLogRepository.save(habitLog);
        habitDto.setDate(targetDate);
        habitDto.setLogged(true);
        return habitDto;

    }
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    private Habit getOwnedHabit(Long habitId, Long userId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit does not exist"));
        if (habit.getUser() == null || !habit.getUser().getId().equals(userId)) {
            throw new SecurityException("Habit does not belong to user");
        }
        return habit;
    }
    private void validateHabitData(HabitDto habitDto, boolean requireId) {
        if (habitDto == null) {
            throw new IllegalArgumentException("HabitDto must not be null");
        }
        if (requireId && habitDto.getId() == null) {
            throw new IllegalArgumentException("Habit id is required for update");
        }
        if (habitDto.getTitle() == null || habitDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Habit title must not be blank");
        }
        String trimmedTitle = habitDto.getTitle().trim();
        if (trimmedTitle.length() < 3 || trimmedTitle.length() > 30) {
            throw new IllegalArgumentException("Habit title must be between 3 and 30 characters");
        }
        if (habitDto.getFrequency() == null || habitDto.getFrequency().isBlank()) {
            throw new IllegalArgumentException("Habit frequency must not be blank");
        }
        String normalizedFreq = habitDto.getFrequency().trim().toUpperCase();
        if (!ALLOWED_FREQUENCIES.contains(normalizedFreq)) {
            throw new IllegalArgumentException("Unsupported frequency: " + habitDto.getFrequency());
        }
        if (habitDto.getCurrentStreak() != null && habitDto.getCurrentStreak() < 0) {
            throw new IllegalArgumentException("Current streak cannot be negative");
        }
        if (habitDto.getLongestStreak() != null && habitDto.getLongestStreak() < 0) {
            throw new IllegalArgumentException("Longest streak cannot be negative");
        }
        if (habitDto.getCurrentStreak() != null && habitDto.getLongestStreak() != null
                && habitDto.getLongestStreak() < habitDto.getCurrentStreak()) {
            throw new IllegalArgumentException("Longest streak cannot be less than current streak");
        }
    }

}
