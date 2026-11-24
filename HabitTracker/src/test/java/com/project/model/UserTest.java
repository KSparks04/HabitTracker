package com.project.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    Habit habit1;
    Habit habit2;

    @BeforeEach
    void setUp() {
        user = new User("test1","test1@gmail.com","DJH");
        habit1 = new Habit("Morning Jog","HelpCircle","DAILY","",true,"#FFFFF");
        habit2 = new Habit("Read 20 Pages","HelpCircle","DAILY","",true,"#FFFFF");
    }

    @AfterEach
    void tearDown() {
        user = null;
        habit1 = null;
        habit2 = null;
    }

    @Test
    void setId() {
        user.setId(134L);
        assertEquals(134L,user.getId());
    }

    @Test
    void getId() {
        user.setId(134L);
        assertEquals(134L,user.getId());
    }

    @Test
    void getUsername() {
        assertEquals("test1",user.getUsername());
    }

    @Test
    void getEmail() {
        assertEquals("test1@gmail.com",user.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("DJH",user.getPassword());
    }

    @Test
    void getCreatedAt() {
        user.setCreatedAt(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX,user.getCreatedAt());
    }

    @Test
    void getHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(habit1);
        habits.add(habit2);
        user.setHabits(habits);
        assertEquals(habits,user.getHabits());
    }

    @Test
    void setUsername() {
        user.setUsername("test2");
        assertEquals("test2",user.getUsername());
    }

    @Test
    void setEmail() {
        user.setEmail("test3@gmail.com");
        assertEquals("test3@gmail.com",user.getEmail());
    }

    @Test
    void setPassword() {
        user.setPassword("DJH1");
        assertEquals("DJH1",user.getPassword());
    }

    @Test
    void setCreatedAt() {
        user.setCreatedAt(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN,user.getCreatedAt());
    }

    @Test
    void setHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(habit1);
        habits.add(habit2);
        user.setHabits(habits);
        assertEquals(habits,user.getHabits());
    }
}