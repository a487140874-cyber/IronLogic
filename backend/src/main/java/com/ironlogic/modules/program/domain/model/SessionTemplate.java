package com.ironlogic.modules.program.domain.model;

import java.time.LocalDateTime;

/**
 * SessionTemplate 的领域模型。
 *
 * <p>SessionTemplate 归属于某个 ProgramBlock，用于定义可复用的训练日模板，
 * 例如 Push Day、Leg Day。
 *
 * @param id SessionTemplate id
 * @param blockId 所属 Block id
 * @param name 模板名称
 * @param sequenceNo 在 Block 内的顺序号
 * @param triggerMode 触发模式
 * @param notes 可选备注
 * @param metadataJson 预留元数据 JSON
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record SessionTemplate(
        Long id,
        Long blockId,
        String name,
        Integer sequenceNo,
        String triggerMode,
        String notes,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
