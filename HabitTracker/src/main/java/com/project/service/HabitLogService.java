package com.project.service;

import com.project.dto.HabitDto;
import com.project.dto.HabitLogDto;
import com.project.dto.HabitWeeklyDto;
import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import com.project.repository.HabitLogRepository;
import com.project.repository.HabitRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitLogService {
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;

    public List<HabitLogDto> findAllHabitLogs(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        List<HabitLog> habitLogs = habitLogRepository.findByHabitId(id);
        List<HabitLogDto> habitLogDtos = new ArrayList<>();
        for(HabitLog habitLog : habitLogs){
            HabitLogDto habitdto =  new HabitLogDto();
            habitdto.setId(habitLog.getId());
            habitdto.setHabitId(habitLog.getHabit().getId());
            habitdto.setCompletedDate(habitLog.getCompletedDate());
            habitLogDtos.add(habitdto);
        }
        return habitLogDtos;
    }
    public HabitLogDto addHabitLog(HabitLogDto habitDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        if (habitDto.getHabitId() == null) {
            throw new IllegalArgumentException("Habit ID must not be null");
        }
        System.out.println("Received habitId: " + habitDto.getHabitId());
        Habit habit = habitRepository.findById(habitDto.getHabitId())
                .orElseThrow(() -> new RuntimeException("Habit not found with id: " + habitDto.getHabitId()));
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        LocalDateTime now = LocalDateTime.now();
        habitLog.setCompletedDate(now);
        habitDto.setCompletedDate(now);
        habitLogRepository.save(habitLog);
        return habitDto;

    }
    public void toggleLogForDate(String userEmail,Long habitId,String dateString){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(()->new RuntimeException("Habit not found with id: " + habitId));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to modify this habit");
        }

        LocalDate localDate = LocalDate.parse(dateString);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndCompletedDateBetween(habitId, startOfDay, endOfDay);
        if (existingLog.isPresent()) {

            habitLogRepository.delete(existingLog.get());
        } else {

            HabitLog habitLog = new HabitLog();
            habitLog.setHabit(habit);
            habitLog.setCompletedDate(startOfDay);
            habitLogRepository.save(habitLog);
        }
    }
    public List<HabitWeeklyDto> getHabitsAndLogs(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Habit> habits = habitRepository.findByUserId(userId);

        List<HabitLog> logs = habitLogRepository.findByHabit_User_IdAndCompletedDateBetween(userId, startDate, endDate);
        List<HabitWeeklyDto> responseDtos = new ArrayList<>();
        for (Habit habit : habits) {
            HabitWeeklyDto dto = new HabitWeeklyDto();
            dto.setId(habit.getId());
            dto.setTitle(habit.getTitle());
            dto.setStreak(calculateCurrentStreak(habit.getId())); // Your streak method!

            // Filter logs belonging to this specific habit and convert to "YYYY-MM-DD" strings
            List<String> completedDates = logs.stream()
                    .filter(log -> log.getHabit().getId().equals(habit.getId()))
                    .map(log -> log.getCompletedDate().toLocalDate().toString())
                    .collect(Collectors.toList());

            dto.setCompletedDates(completedDates);
            responseDtos.add(dto);
        }
        return  responseDtos;
    }
    public int calculateLongestStreak(Long habitDtoId){
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByCompletedDateDesc(habitDtoId);
        if(logs.isEmpty()){return 0;}
        int maxStreak = 0;
        int currentStreak = 0;
        LocalDate expectedDate = null;

        if (!logs.get(0).getCompletedDate().toLocalDate().equals(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
        }
        for (HabitLog habitLog : logs) {
            LocalDate logDate = habitLog.getCompletedDate().toLocalDate();
            if (expectedDate == null) {

                currentStreak = 1;
                expectedDate = logDate.minusDays(1);
            } else if (logDate.equals(expectedDate)) {

                currentStreak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (logDate.equals(expectedDate.plusDays(1))) {

                continue;
            } else {

                maxStreak = Math.max(maxStreak, currentStreak);
                currentStreak = 1;
                expectedDate = logDate.minusDays(1);
            }
        }
        return Math.max(maxStreak, currentStreak);

    }
    public int calculateCurrentStreak(Long habitDtoId){
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByCompletedDateDesc(habitDtoId);
        if(logs.isEmpty()){return 0;}
        int streak = 0;
        LocalDate expectedDate = LocalDate.now();
        if (!logs.get(0).getCompletedDate().toLocalDate().equals(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
        }
        for (HabitLog habitLog : logs) {
            LocalDate logDate = habitLog.getCompletedDate().toLocalDate();
            if (logDate.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            }
            else if (logDate.isBefore(expectedDate)) {

                break;
            }
        }
        return streak;

    }
}
