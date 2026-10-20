package com.project.controller;

import com.project.model.HabitTemplate;
import com.project.service.HabitService;
import com.project.service.HabitTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController

@CrossOrigin(origins = "http://localhost:5173")
public class HabitTemplateController {
    @Autowired
    private HabitTemplateService habitTemplateService;

    @GetMapping("/api/templates")
    public ResponseEntity<List<HabitTemplate>> getAllHabitTemplates() {
        List<HabitTemplate>  habitTemplates = habitTemplateService.getAllHabitTemplates();
        return new ResponseEntity<>(habitTemplates, HttpStatus.OK);
    }
    @GetMapping("/api/templates/category/{name}")
    public ResponseEntity<List<HabitTemplate>> getHabitTemplatesByCategory(@PathVariable String name) {
        List<HabitTemplate> habitTemplates = habitTemplateService.getHabitTemplatesByCategory(name);
        return new ResponseEntity<>(habitTemplates, HttpStatus.OK);
    }
}
