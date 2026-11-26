package com.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitDto {
    private Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    private String title;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
    private LocalDateTime date;
    private Boolean logged;
    @Size(max = 20)
    private String color;
    @Size(max = 50)
    private String iconName;
    private List<LocalDateTime> completedDates;
    @PositiveOrZero
    private Integer currentStreak;
    @PositiveOrZero
    private Integer longestStreak;
}
