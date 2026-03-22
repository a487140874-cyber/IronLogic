package com.ironlogic.modules.workout.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to {@code workout_exercises}.
 */
@TableName("workout_exercises")
public class WorkoutExerciseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("workout_session_id")
    private Long workoutSessionId;

    @TableField("exercise_id")
    private Long exerciseId;

    @TableField("source_template_exercise_id")
    private Long sourceTemplateExerciseId;

    @TableField("actual_order_no")
    private Integer actualOrderNo;

    @TableField("replacement_of_exercise_id")
    private Long replacementOfExerciseId;

    private String notes;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkoutSessionId() { return workoutSessionId; }
    public void setWorkoutSessionId(Long workoutSessionId) { this.workoutSessionId = workoutSessionId; }
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Long getSourceTemplateExerciseId() { return sourceTemplateExerciseId; }
    public void setSourceTemplateExerciseId(Long sourceTemplateExerciseId) { this.sourceTemplateExerciseId = sourceTemplateExerciseId; }
    public Integer getActualOrderNo() { return actualOrderNo; }
    public void setActualOrderNo(Integer actualOrderNo) { this.actualOrderNo = actualOrderNo; }
    public Long getReplacementOfExerciseId() { return replacementOfExerciseId; }
    public void setReplacementOfExerciseId(Long replacementOfExerciseId) { this.replacementOfExerciseId = replacementOfExerciseId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
