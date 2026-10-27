package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLogDto {
    private Long id;
    private Long habitId;
    private String title;
    private List<LocalDateTime> completedDates;
    private LocalDateTime completedDate;
    private Integer currentStreak;
    private Integer longestStreak;
}
