package com.ironlogic.modules.progression.dto;

import java.time.LocalDateTime;

/**
 * 对外暴露 ProgramProgress 状态的 DTO。
 *
 * @param id 推进记录 id
 * @param userId 所有者用户 id
 * @param programId 目标 Program id
 * @param currentBlockId 当前停留的 Block id
 * @param nextSessionTemplateId 下一个推荐 SessionTemplate id
 * @param lastCompletedWorkoutId 最近一次推进该状态的已完成训练 id
 * @param sequenceCursor 简化后的推进计数
 * @param progressSnapshotJson 轻量推进快照 JSON
 * @param updatedAt 更新时间
 */
public record ProgramProgressResponse(
        Long id,
        Long userId,
        Long programId,
        Long currentBlockId,
        Long nextSessionTemplateId,
        Long lastCompletedWorkoutId,
        Integer sequenceCursor,
        String progressSnapshotJson,
        LocalDateTime updatedAt
) {
}
