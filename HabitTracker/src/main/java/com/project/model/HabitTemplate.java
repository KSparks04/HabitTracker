package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habit_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HabitTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String category;
    @Column(nullable=false)
    private String iconName;
    @Column(name="default_frequency",nullable = false)
    private String default_frequency;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
