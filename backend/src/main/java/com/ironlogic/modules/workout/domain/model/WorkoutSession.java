package com.ironlogic.modules.workout.domain.model;

import java.time.LocalDateTime;

/**
 * 一次实际 WorkoutSession 的领域模型。
 *
 * <p>它是模板定义层在执行层的对应物，用于记录用户的一次真实训练，并可选地回溯到
 * 创建它的 Program / Block / SessionTemplate。
 *
 * @param id WorkoutSession id
 * @param userId 所有者用户 id
 * @param sourceType 训练来源是模板还是手动创建
 * @param sourceProgramId 可选来源 Program id
 * @param sourceBlockId 可选来源 Block id
 * @param sourceTemplateId 可选来源 SessionTemplate id
 * @param status 当前训练状态
 * @param startedAt 实际开始时间
 * @param endedAt 实际结束时间
 * @param notes 可选训练备注
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record WorkoutSession(
        Long id,
        Long userId,
        WorkoutSourceType sourceType,
        Long sourceProgramId,
        Long sourceBlockId,
        Long sourceTemplateId,
        WorkoutStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
