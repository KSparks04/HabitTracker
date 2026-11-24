package com.project.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HabitTemplateTest {
    HabitTemplate  habitTemplate;
    @BeforeEach
    void setUp() {
        habitTemplate = new HabitTemplate("Morning Jog","FITNESS","HelpCircle","WEEKLY");
    }

    @AfterEach
    void tearDown() {
        habitTemplate = null;
    }

    @Test
    void setId() {
        habitTemplate.setId(123L);
        assertEquals(123L,habitTemplate.getId());
    }

    @Test
    void getId() {
        habitTemplate.setId(123L);
        assertEquals(123L,habitTemplate.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Morning Jog",habitTemplate.getTitle());
    }

    @Test
    void getCategory() {
        assertEquals("FITNESS",habitTemplate.getCategory());
    }

    @Test
    void getIconName() {
        assertEquals("HelpCircle",habitTemplate.getIconName());
    }

    @Test
    void getDefault_frequency() {
        assertEquals("WEEKLY",habitTemplate.getDefault_frequency());
    }

    @Test
    void setTitle() {
        habitTemplate.setTitle("Morning Run");
        assertEquals("Morning Run",habitTemplate.getTitle());
    }

    @Test
    void setCategory() {
        habitTemplate.setCategory("HEALTH");
        assertEquals("HEALTH",habitTemplate.getCategory());

    }

    @Test
    void setIconName() {
        habitTemplate.setIconName("HelpSquare");
        assertEquals("HelpSquare",habitTemplate.getIconName());
    }

    @Test
    void setDefault_frequency() {
        habitTemplate.setDefault_frequency("DAILY");
        assertEquals("DAILY",habitTemplate.getDefault_frequency());
    }
}