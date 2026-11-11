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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
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
    public void addLogNote(HabitLogDto habitLogDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        if (habitLogDto.getHabitId() == null) {
            throw new IllegalArgumentException("Habit ID must not be null");
        }
        List<HabitLog> log = habitLogRepository.findLatestLogsForHabit(habitLogDto.getHabitId(),PageRequest.of(0,1));
        HabitLog existingLog = log.get(0);

        if (existingLog != null) {


            existingLog.setNotes(habitLogDto.getNote());
            habitLogRepository.save(existingLog);
        } else {
            throw new IllegalArgumentException("Habit Log with id " + habitLogDto.getId() + " does not exist");
        }

    }
    public List<HabitLogDto> getRecentHabits(Long userId){
        List<HabitLog> logs =
                habitLogRepository.findLatestLogPerHabitByUserId(
                        userId,
                        PageRequest.of(0, 5)
                );
        List<HabitLogDto> responseDtos =  new ArrayList<>();
        for (HabitLog habitLog : logs) {
            HabitLogDto habitLogDto = new HabitLogDto();
            habitLogDto.setHabitId(habitLog.getId());
            habitLogDto.setCompletedDate(habitLog.getCompletedDate());
            habitLogDto.setTitle(habitLog.getHabit().getTitle());
            habitLogDto.setIconName(habitLog.getHabit().getIconName());
            habitLogDto.setFrequency(habitLog.getHabit().getFrequency());
            responseDtos.add(habitLogDto);
        }
        return responseDtos;

    }
    public void toggleLogForDate(String userEmail,Long habitId,String dateString){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(()->new RuntimeException("Habit not found with id: " + habitId));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to modify this habit");
        }
        System.out.println(habitId);

        LocalDate localDate = LocalDate.parse(dateString);

        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime nextDay = localDate.plusDays(1).atStartOfDay();

        Optional<HabitLog> existingLog =
                habitLogRepository.findLogForDate(
                        habitId,
                        startOfDay,
                        nextDay
                );


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


            List<String> completedDates = logs.stream()
                    .filter(log -> log.getHabit().getId().equals(habit.getId()))
                    .map(log -> log.getCompletedDate().toLocalDate().toString())
                    .collect(Collectors.toList());

            dto.setCompletedDates(completedDates);
            responseDtos.add(dto);
        }
        return  responseDtos;
    }

    public List<HabitLogDto> getHabitsLogsWeek(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Habit> habits = habitRepository.findByUserId(userId);

        List<HabitLog> logs = habitLogRepository.findByHabit_User_IdAndCompletedDateBetween(userId, startDate, endDate);
        List<HabitLogDto> responseDtos = new ArrayList<>();
        for (Habit habit : habits) {
            HabitLogDto dto = new HabitLogDto();
            dto.setId(habit.getId());
            dto.setTitle(habit.getTitle());
            dto.setStreak(calculateDaysOutofWeeks(habit.getId())); // Your streak method!
            dto.setIconName(habit.getIconName());
            List<String> completedDates = logs.stream()
                    .filter(log -> log.getHabit().getId().equals(habit.getId()))
                    .map(log -> log.getCompletedDate().toLocalDate().toString())
                    .collect(Collectors.toList());


            responseDtos.add(dto);
        }
        return  responseDtos;
    }
    public List<HabitLogDto> getLongestHabits(Long userId){
        List<HabitLog> logs =
                habitLogRepository.findLatestLogPerHabitByUserId(
                        userId,
                        PageRequest.of(0, 5)
                );
        List<HabitLogDto> responseDtos =  new ArrayList<>();
        for (HabitLog habitLog : logs) {
            HabitLogDto habitLogDto = new HabitLogDto();
            habitLogDto.setHabitId(habitLog.getId());
            habitLogDto.setCompletedDate(habitLog.getCompletedDate());
            habitLogDto.setTitle(habitLog.getHabit().getTitle());
            habitLogDto.setIconName(habitLog.getHabit().getIconName());
            habitLogDto.setFrequency(habitLog.getHabit().getFrequency());
            int streak = calculateLongestStreak(habitLog.getHabit().getId());
            habitLogDto.setLongestStreak(streak);
            responseDtos.add(habitLogDto);
        }
        return responseDtos;

    }

    public int calculateDaysOutofWeeks(Long habitDtoId){
        List<HabitLog> logs =
                habitLogRepository.findByHabitIdOrderByCompletedDateDesc(habitDtoId);

        if (logs.isEmpty()) {
            return 0;
        }

        List<LocalDate> dates = logs.stream()
                .map(log -> log.getCompletedDate().toLocalDate())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        LocalDate today = LocalDate.now();
        DayOfWeek weekStart = DayOfWeek.SUNDAY;

        LocalDate startDate =  today.with(TemporalAdjusters.previousOrSame(weekStart));
        LocalDate endDate = startDate.plusDays(6);


        return (int) logs.stream()
                .map(log -> log.getCompletedDate().toLocalDate())
                .distinct()
                .filter(date -> !date.isBefore(startDate))
                .filter(date -> !date.isAfter(endDate))
                .count();
    }
    public int calculateLongestStreak(Long habitDtoId){
        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByCompletedDateDesc(habitDtoId);
        if(logs.isEmpty()){return 0;}
        List<LocalDate> dates = logs.stream()
                .map(log -> log.getCompletedDate().toLocalDate())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        int currentStreak = 1;
        int maxStreak = 1;

        for (int i = 1; i < dates.size(); i++) {

            LocalDate previousDate = dates.get(i - 1);
            LocalDate currentDate = dates.get(i);

            if (currentDate.equals(previousDate.minusDays(1))) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }

            maxStreak = Math.max(maxStreak, currentStreak);
        }

        return maxStreak;

    }
    public int calculateCurrentStreak(Long habitDtoId){
        List<HabitLog> logs =
                habitLogRepository.findByHabitIdOrderByCompletedDateDesc(habitDtoId);

        if (logs.isEmpty()) {
            return 0;
        }

        List<LocalDate> dates = logs.stream()
                .map(log -> log.getCompletedDate().toLocalDate())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        LocalDate today = LocalDate.now();
        LocalDate expectedDate = dates.get(0);
        if (expectedDate.isBefore(today)) {
            if (!expectedDate.equals(today.minusDays(1))) {
                return 0;
            }
        }

        int streak = 0;

        for (LocalDate date : dates) {

            if (date.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;

    }
}
