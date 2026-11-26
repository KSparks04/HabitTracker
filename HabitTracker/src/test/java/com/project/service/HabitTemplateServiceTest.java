package com.project.service;

import com.project.model.HabitTemplate;
import com.project.repository.HabitTemplateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitTemplateServiceTest {
    @Mock
    HabitTemplateRepository habitTemplateRepository;
    @InjectMocks
    HabitTemplateService habitTemplateService;
    List<HabitTemplate> templates;

    @BeforeEach
    public void setUp(){
        templates = new ArrayList<>();

        templates.add(new HabitTemplate(
                "Morning Jog",
                "Fitness",
                "Footprints",
                "DAILY"
        ));

        templates.add(new HabitTemplate(
                "Read 20 Pages",
                "Learning",
                "BookOpen",
                "DAILY"
        ));

        templates.add(new HabitTemplate(
                "Drink 2L Water",
                "Health",
                "Droplet",
                "DAILY"
        ));

        templates.add(new HabitTemplate(
                "Practice a Language",
                "Learning",
                "Languages",
                "WEEKLY"
        ));

        templates.add(new HabitTemplate(
                "Clean Your Room",
                "Productivity",
                "Sparkles",
                "WEEKLY"
        ));


    }

    @Test
    void getAllHabitTemplates() {
        when(habitTemplateRepository.findAll()).thenReturn(templates);
        List<HabitTemplate> returnedTemplates =  habitTemplateService.getAllHabitTemplates();
        assertEquals(templates,returnedTemplates);

    }

    @Test
    void getHabitTemplatesByCategory() {
        String category = "Learning";

        List<HabitTemplate> expectedTemplates = new ArrayList<>();
        expectedTemplates.add(templates.get(1));
        expectedTemplates.add(templates.get(3));

        when(habitTemplateRepository.findByCategory(category))
                .thenReturn(expectedTemplates);
        List<HabitTemplate> returnedTemplates =
                habitTemplateService.getHabitTemplatesByCategory(category);
        assertEquals(expectedTemplates, returnedTemplates);

        verify(habitTemplateRepository).findByCategory(category);
    }

    }
