package com.project.controller;

import com.project.dto.HabitDto;
import com.project.dto.HabitLogDto;
import com.project.dto.HabitWeeklyDto;
import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import com.project.repository.HabitLogRepository;
import com.project.repository.UserRepository;
import com.project.service.HabitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class HabitLogController {
    @Autowired
    private HabitLogService habitLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;

    @PostMapping("/api/habit/log")
    public ResponseEntity<HabitLogDto> createHabitLog(@RequestBody HabitLogDto habitDto, Principal principal) {
        String userEmail = principal.getName();
        HabitLogDto habit = habitLogService.addHabitLog(habitDto,userEmail);
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }
    @GetMapping("/api/habit/logs/{id}")
    public ResponseEntity<List<HabitLogDto>> getHabitLogs(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        List<HabitLogDto> habits = habitLogService.findAllHabitLogs(id,userEmail);
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }
    @GetMapping("/api/habit/logs/recent")
    public ResponseEntity<List<HabitLog>>  getRecentHabitLogs(Principal principal) {
        String userEmail = principal.getName();
        Optional<User> user  = userRepository.findByEmail(userEmail);
        List <HabitLog> logs = habitLogRepository.findTop5ByHabit_User_IdOrderByCompletedDateDesc(user.get().getId());
        return new ResponseEntity<>(logs,HttpStatus.OK);
    }
    @GetMapping("/api/habit/logs/current-streak")
    public ResponseEntity<Integer> getCurrentHabitLogs(HabitLogDto habitLogDto,Principal principal) {
        String userEmail = principal.getName();
        int currentStreak = habitLogService.calculateCurrentStreak(habitLogDto.getHabitId());
        return new ResponseEntity<>(currentStreak,HttpStatus.OK);
    }
    @GetMapping("/api/habit/logs/{habitId}")
    public ResponseEntity<List<HabitLog>> getLogsForDateRange(@PathVariable Long habitId,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        List<HabitLog> logs =  habitLogRepository.findByHabit_User_IdAndCompletedDateBetween(habitId, startDate, endDate);
        return new ResponseEntity<>(logs,HttpStatus.OK);
    }
    @GetMapping("/api/habits/weekly")
    public ResponseEntity<List<HabitWeeklyDto>> getUserHabitLogs(Principal principal,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        String userEmail = principal.getName();
        Optional<User> user  = userRepository.findByEmail(userEmail);
        List<HabitWeeklyDto> logs = habitLogService.getHabitsAndLogs(user.get().getId(), startDate,endDate);
        return new ResponseEntity<>(logs,HttpStatus.OK);
    }
    @PostMapping("/api/habits/logs/{habitId}/toggle")
    public ResponseEntity<Void> updateUserHabitLogs(Principal principal,@PathVariable Long habitId,@RequestParam String date){
        String userEmail = principal.getName();
        habitLogService.toggleLogForDate(userEmail,habitId,date);
        return ResponseEntity.ok().build();
    }

}
