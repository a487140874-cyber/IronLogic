package com.ironlogic.modules.workout.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to {@code workout_sets}.
 */
@TableName("workout_sets")
public class WorkoutSetEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("workout_exercise_id")
    private Long workoutExerciseId;

    @TableField("set_no")
    private Integer setNo;

    private BigDecimal weight;

    private Integer reps;

    @TableField("duration_seconds")
    private Integer durationSeconds;

    @TableField("rest_seconds")
    private Integer restSeconds;

    private BigDecimal rpe;

    private Integer rir;

    @TableField("is_warmup")
    private Boolean isWarmup;

    @TableField("is_completed")
    private Boolean isCompleted;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkoutExerciseId() { return workoutExerciseId; }
    public void setWorkoutExerciseId(Long workoutExerciseId) { this.workoutExerciseId = workoutExerciseId; }
    public Integer getSetNo() { return setNo; }
    public void setSetNo(Integer setNo) { this.setNo = setNo; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public Integer getRestSeconds() { return restSeconds; }
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    public BigDecimal getRpe() { return rpe; }
    public void setRpe(BigDecimal rpe) { this.rpe = rpe; }
    public Integer getRir() { return rir; }
    public void setRir(Integer rir) { this.rir = rir; }
    public Boolean getIsWarmup() { return isWarmup; }
    public void setIsWarmup(Boolean warmup) { isWarmup = warmup; }
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean completed) { isCompleted = completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
