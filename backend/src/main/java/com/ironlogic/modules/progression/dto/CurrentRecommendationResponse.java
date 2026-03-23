package com.ironlogic.modules.progression.dto;

/**
 * 某个 Program 当前序列推荐结果的 DTO。
 *
 * @param programId 目标 Program id
 * @param currentBlockId 当前推荐所在的 Block id
 * @param nextSessionTemplateId 下一个推荐 SessionTemplate id
 * @param sequenceCursor 简化后的推进游标；如果还没有 ProgramProgress，则默认为 0
 * @param recommendedSessionTemplate 完整推荐模板及其模板动作目标值
 */
public record CurrentRecommendationResponse(
        Long programId,
        Long currentBlockId,
        Long nextSessionTemplateId,
        Integer sequenceCursor,
        RecommendedSessionTemplateResponse recommendedSessionTemplate
) {
}
