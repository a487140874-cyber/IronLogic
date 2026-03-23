package com.ironlogic.modules.progression.domain.model;

import java.time.LocalDateTime;

/**
 * ProgramProgress 的领域模型。
 *
 * <p>这个对象保存“某个用户在某个 Program 上的轻量推进状态”。它故意不替代 workout 历史；
 * workout 历史仍然是实际发生过什么的事实来源，而 ProgramProgress 只保存推荐游标。
 *
 * @param id 推进记录 id
 * @param userId 所有者用户 id
 * @param programId 目标 Program id
 * @param currentBlockId 当前停留的 Block id
 * @param nextSessionTemplateId 下一个推荐 SessionTemplate id
 * @param lastCompletedWorkoutId 最近一次推进该状态的已完成训练 id
 * @param sequenceCursor 简化后的序列推进计数器
 * @param progressSnapshotJson 预留给调试和扩展的快照 JSON
 * @param updatedAt 更新时间
 */
public record ProgramProgress(
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
