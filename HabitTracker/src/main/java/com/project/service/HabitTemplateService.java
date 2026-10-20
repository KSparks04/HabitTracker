package com.project.service;

import com.project.model.Habit;
import com.project.model.HabitTemplate;
import com.project.repository.HabitTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitTemplateService {
    @Autowired
    private HabitTemplateRepository habitTemplateRepository;
    public List<HabitTemplate> getAllHabitTemplates() {
        return  habitTemplateRepository.findAll();
    }
    public  List<HabitTemplate> getHabitTemplatesByCategory(String category) {
        return habitTemplateRepository.findByCategory(category);
    }
}

