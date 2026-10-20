package com.project.repository;

import com.project.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findAll();
    List<Habit> findByUserId(Long userId);
    List<Habit> findBySaved(Boolean saved);

}
