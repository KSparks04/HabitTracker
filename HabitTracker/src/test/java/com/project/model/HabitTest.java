package com.project.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {
    Habit habit;
    User user;
    @BeforeEach
    void setUp() {
        habit = new Habit("Morning Jog","HelpCircle","DAILY","",true,"#FFFFF");
        user = new User("test1","test1@gmail.com","JDjh66");
    }

    @AfterEach
    void tearDown() {
        habit = null;
        user = null;
    }

    @Test
    void setId() {
        habit.setId(15L);
        assertEquals(15,habit.getId());
    }

    @Test
    void getId() {
        habit.setId(15L);
        assertEquals(15,habit.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Morning Jog",habit.getTitle());
    }

    @Test
    void getIconName() {
        assertEquals("HelpCircle",habit.getIconName());
    }

    @Test
    void getFrequency() {
        assertEquals("DAILY",habit.getFrequency());
    }

    @Test
    void getNotes() {
        assertEquals("",habit.getNotes());
    }

    @Test
    void getSaved() {
        assertEquals(true,habit.getSaved());
    }

    @Test
    void getColor() {
        assertEquals("#FFFFF",habit.getColor());
    }

    @Test
    void getCreatedAt() {
        habit.setCreatedAt(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX,habit.getCreatedAt());
    }

    @Test
    void getUser() {
        habit.setUser(user);
        assertEquals(user,habit.getUser());

    }

    @Test
    void setTitle() {
        habit.setTitle("New Title");
        assertEquals("New Title",habit.getTitle());
    }

    @Test
    void setIconName() {
        habit.setIconName("New Icon");
        assertEquals("New Icon",habit.getIconName());
    }

    @Test
    void setFrequency() {
        habit.setFrequency("WEEKLY");
        assertEquals("WEEKLY",habit.getFrequency());
    }

    @Test
    void setNotes() {
        habit.setNotes("New Notes");
        assertEquals("New Notes",habit.getNotes());
    }

    @Test
    void setSaved() {
        habit.setSaved(false);
        assertEquals(false,habit.getSaved());
    }

    @Test
    void setColor() {
        habit.setColor("#FFFFEF");
        assertEquals("#FFFFEF",habit.getColor());
    }

    @Test
    void setCreatedAt() {
        habit.setCreatedAt(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN,habit.getCreatedAt());
    }

    @Test
    void setUser() {
        habit.setUser(user);
        assertEquals(user,habit.getUser());
    }
}