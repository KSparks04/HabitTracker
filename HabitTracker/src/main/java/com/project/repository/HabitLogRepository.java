package com.project.repository;

import com.project.model.Habit;
import com.project.model.HabitLog;
import com.project.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog,Long> {
    List<HabitLog> findByHabitId(Long habitId);
    Optional<HabitLog> findById(Long logId);
    Optional<HabitLog> findByHabitIdAndCompletedDate(Long habitId, LocalDateTime completedDate);
    List<HabitLog> findByHabitIdOrderByCompletedDateDesc(Long habitId);
    @Query("""
    SELECT hl FROM HabitLog hl
    WHERE hl.habit.user.id = :userId
    AND hl.completedDate = (
        SELECT MAX(hl2.completedDate)
        FROM HabitLog hl2
        WHERE hl2.habit.id = hl.habit.id
    )
    ORDER BY hl.completedDate DESC
""")
    List<HabitLog> findLatestLogPerHabitByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );
    @Query("""
    SELECT hl FROM HabitLog hl
    WHERE hl.habit.id = :habitId
    ORDER BY hl.completedDate DESC
""")
    List<HabitLog> findLatestLogsForHabit(@Param("habitId") Long habitId, Pageable pageable);
    Optional<HabitLog> findByHabit_IdAndCompletedDateBetween(
            Long habitId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
    @Query("""
    SELECT hl FROM HabitLog hl
    WHERE hl.habit.id = :habitId
    AND hl.completedDate >= :start
    AND hl.completedDate < :end
""")
    Optional<HabitLog> findLogForDate(
            @Param("habitId") Long habitId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<HabitLog> findByHabit_User_IdAndCompletedDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    void deleteByHabitId(Long habitId);
}
