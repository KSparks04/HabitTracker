package com.project.repository;

import com.project.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog,Long> {
    List<HabitLog> findByHabitId(Long habitId);
    Optional<HabitLog> findByHabitIdAndCompletedDate(Long habitId, LocalDateTime completedDate);

}
