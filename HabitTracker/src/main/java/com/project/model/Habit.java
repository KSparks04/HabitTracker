package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "habits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable=false)
    private String iconName;
    @Column(nullable = false)
    private String frequency;
    private String notes;
    @Column(nullable = false)
    private Boolean saved;
    @Column(nullable = false)
    private String color;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;
    public Habit(String title, String iconName, String frequency, String notes, Boolean saved, String color) {
        this.title = title;
        this.iconName = iconName;
        this.frequency = frequency;
        this.notes = notes;
        this.saved = saved;
        this.color = color;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
