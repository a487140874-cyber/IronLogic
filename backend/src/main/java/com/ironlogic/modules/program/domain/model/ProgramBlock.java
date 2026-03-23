package com.ironlogic.modules.program.domain.model;

import java.time.LocalDateTime;

/**
 * ProgramBlock 的领域模型。
 *
 * <p>ProgramBlock 归属于某个 Program，用于描述更大的训练阶段，例如增肌期或力量期。
 * 这里保留 sequenceNo，是因为 IronLogic 更偏向基于序列推进，而不是纯日历推进。
 *
 * @param id Block id
 * @param programId 所属 Program id
 * @param name Block 名称
 * @param blockType Block 类型
 * @param sequenceNo 在 Program 内的顺序号
 * @param durationMode 时长模式
 * @param durationValue 可选时长值
 * @param deloadEnabled 是否启用减量能力
 * @param metadataJson 预留元数据 JSON
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record ProgramBlock(
        Long id,
        Long programId,
        String name,
        String blockType,
        Integer sequenceNo,
        String durationMode,
        Integer durationValue,
        Boolean deloadEnabled,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
