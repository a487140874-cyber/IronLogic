package com.ironlogic.modules.progression.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 当前推荐 SessionTemplate 的 DTO。
 *
 * @param id SessionTemplate id
 * @param blockId 所属 Block id
 * @param name 模板名称
 * @param sequenceNo 在 Block 内的顺序号
 * @param triggerMode 模板配置的触发模式
 * @param notes 可选模板备注
 * @param metadataJson 预留模板元数据 JSON
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 * @param exercises 该模板下排序后的推荐动作列表
 */
public record RecommendedSessionTemplateResponse(
        Long id,
        Long blockId,
        String name,
        Integer sequenceNo,
        String triggerMode,
        String notes,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<RecommendedExerciseResponse> exercises
) {
}
