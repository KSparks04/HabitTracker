package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "habit_logs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"habit_id", "completed_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "completed_date",nullable = false)
    private LocalDateTime completedDate;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="habit_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Habit habit;
    @Column(name="notes")
    private String notes;




    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
