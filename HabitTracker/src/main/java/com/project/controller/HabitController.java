package com.project.controller;

import com.project.dto.HabitDto;
import com.project.model.Habit;
import com.project.repository.HabitRepository;
import com.project.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController


public class HabitController {

    @Autowired
    private HabitService  habitService;
    @GetMapping("/api/habits")
    public ResponseEntity<List<HabitDto>> getUserHabits(Principal principal) {
        String userEmail = principal.getName();
        List<HabitDto> habits = habitService.getHabitsForUser(userEmail);
        return  new ResponseEntity<>(habits, HttpStatus.OK);
    }
    @PostMapping("/api/habits")
    public ResponseEntity<HabitDto> createUserHabit(@RequestBody HabitDto habitDto, Principal principal) {
        String userEmail = principal.getName();
        HabitDto habit = habitService.createHabitForUser(habitDto, userEmail);
        return  new ResponseEntity<>(habit, HttpStatus.CREATED);

    }
    @PutMapping("/api/habits/{id}")
    public ResponseEntity<HabitDto> updateUserHabit(@PathVariable Long id, @RequestBody HabitDto habitDto, Principal principal) {
        String userEmail = principal.getName();
        HabitDto habit = habitService.updateHabitForUser(id,habitDto, userEmail);
        return  new ResponseEntity<>(habit, HttpStatus.OK);
    }
    @DeleteMapping("/api/habits/{id}")
    public ResponseEntity<String> deleteUserHabit( @PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        String habit = habitService.deleteHabitForUser(id,userEmail);
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }
    @PostMapping("/api/habits/{id}/toggle")
    public ResponseEntity<HabitDto> toggleUserHabit(@RequestBody  HabitDto habitDto,@PathVariable Long id ,Principal principal) {
        String userName = principal.getName();
        HabitDto habit = habitService.toggleHabitLog(habitDto,userName);
        return  new ResponseEntity<>(habit, HttpStatus.OK);
    }
}

