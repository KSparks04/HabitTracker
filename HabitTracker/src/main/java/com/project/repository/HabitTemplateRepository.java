package com.project.repository;

import com.project.model.HabitTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitTemplateRepository extends JpaRepository<HabitTemplate,Long> {
    List<HabitTemplate> findAll();
    List<HabitTemplate> findByCategory(String category);
    Optional<HabitTemplate> findByTitle(String title);
}
