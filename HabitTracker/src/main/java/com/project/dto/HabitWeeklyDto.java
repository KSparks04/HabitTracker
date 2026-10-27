package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitWeeklyDto {

        private Long id;
        private String title;
        private int streak;
        private List<String> completedDates;

}
