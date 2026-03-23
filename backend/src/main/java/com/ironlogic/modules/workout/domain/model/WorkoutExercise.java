package com.ironlogic.modules.workout.domain.model;

import java.time.LocalDateTime;

/**
 * WorkoutSession 中单个训练动作的领域模型。
 *
 * <p>WorkoutExercise 可以来自模板复制，也可以在训练过程中手动新增。
 *
 * @param id WorkoutExercise id
 * @param workoutSessionId 所属 WorkoutSession id
 * @param exerciseId 引用的 Exercise id
 * @param sourceTemplateExerciseId 可选来源模板动作 id
 * @param actualOrderNo 在训练中的实际顺序
 * @param replacementOfExerciseId 预留给未来动作替换逻辑的字段
 * @param notes 可选动作备注
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record WorkoutExercise(
        Long id,
        Long workoutSessionId,
        Long exerciseId,
        Long sourceTemplateExerciseId,
        Integer actualOrderNo,
        Long replacementOfExerciseId,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
