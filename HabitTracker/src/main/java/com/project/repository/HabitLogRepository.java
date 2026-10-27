package com.project.repository;

import com.project.model.HabitLog;
import com.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog,Long> {
    List<HabitLog> findByHabitId(Long habitId);
    Optional<HabitLog> findByHabitIdAndCompletedDate(Long habitId, LocalDateTime completedDate);
    List<HabitLog> findByHabitIdOrderByCompletedDateDesc(Long habitId);
    List<HabitLog> findTop5ByHabit_User_IdOrderByCompletedDateDesc(Long userId);
    Optional<HabitLog> findByHabitIdAndCompletedDateBetween(
            Long habitId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    List<HabitLog> findByHabit_User_IdAndCompletedDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

}
