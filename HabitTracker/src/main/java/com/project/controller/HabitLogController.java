package com.project.controller;

import com.project.dto.HabitDto;
import com.project.repository.HabitLogRepository;
import com.project.service.HabitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HabitLogController {
    @Autowired
    private HabitLogService habitLogService;
    @PostMapping("/api/habit/log")
    public ResponseEntity<HabitDto> createHabitLog(@RequestBody HabitDto habitDto, Principal principal) {
        String userEmail = principal.getName();
        HabitDto habit = habitLogService.addHabitLog(habitDto,userEmail);
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }
}
