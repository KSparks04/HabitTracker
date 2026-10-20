package com.project.dto;

import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitDto {
    private Long id;
    private String title;
    private String frequency;
    private String notes;
    private LocalDateTime date;
    private Boolean logged;
    private String color;
    private String iconName;
    private List<LocalDateTime> completedDates;

    private Integer currentStreak;
    private Integer longestStreak;
}
