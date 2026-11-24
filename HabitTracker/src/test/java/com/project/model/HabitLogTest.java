package com.project.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HabitLogTest {
    HabitLog habitLog;
    Habit habit;
    LocalDateTime completedDate;
    @BeforeEach
    void setUp() {
        completedDate = LocalDateTime.now();
        habit = new Habit("Morning Jog","HelpCircle","DAILY","",true,"#FFFFF");
        habitLog = new HabitLog(habit,"Felt good today!",completedDate);
    }

    @AfterEach
    void tearDown() {
        habitLog = null;
        habit = null;
        completedDate = null;
    }

    @Test
    void setId() {
        habitLog.setId(128L);
        assertEquals(128L,habitLog.getId());
    }

    @Test
    void getId() {
        habitLog.setId(128L);
        assertEquals(128L,habitLog.getId());
    }

    @Test
    void getCompletedDate() {
        assertEquals(completedDate,habitLog.getCompletedDate());
    }

    @Test
    void getHabit() {
        assertEquals(habit,habitLog.getHabit());
    }

    @Test
    void getNotes() {
        assertEquals("Felt good today!",habitLog.getNotes());
    }

    @Test
    void setCompletedDate() {
        LocalDateTime completedDate = LocalDateTime.now();
        habitLog.setCompletedDate(completedDate);
        assertEquals(completedDate,habitLog.getCompletedDate());
    }

    @Test
    void setHabit() {
        Habit habit1 = new Habit("Morning","HelpCircle","DAILY","",true,"#FFFFF");
        habitLog.setHabit(habit1);
        assertEquals(habit1,habitLog.getHabit());
    }

    @Test
    void setNotes() {
        habitLog.setNotes("Amazing run!");
        assertEquals("Amazing run!",habitLog.getNotes());
    }
}