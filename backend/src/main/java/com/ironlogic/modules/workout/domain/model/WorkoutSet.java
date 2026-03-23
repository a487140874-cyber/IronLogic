package com.ironlogic.modules.workout.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * WorkoutExercise 下单组记录的领域模型。
 *
 * <p>WorkoutSet 是训练执行中的最小记录单元。MVP 先采用整列表覆盖，而不是更细粒度的
 * patch 写法，以保持写入逻辑简单易懂。
 *
 * @param id WorkoutSet id
 * @param workoutExerciseId 所属 WorkoutExercise id
 * @param setNo 在动作内的组序号
 * @param weight 可选记录重量
 * @param reps 可选记录次数
 * @param durationSeconds 可选时长结果
 * @param restSeconds 可选休息秒数
 * @param rpe 可选主观用力程度
 * @param rir 可选剩余次数
 * @param isWarmup 是否热身组
 * @param isCompleted 是否完成
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record WorkoutSet(
        Long id,
        Long workoutExerciseId,
        Integer setNo,
        BigDecimal weight,
        Integer reps,
        Integer durationSeconds,
        Integer restSeconds,
        BigDecimal rpe,
        Integer rir,
        Boolean isWarmup,
        Boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
