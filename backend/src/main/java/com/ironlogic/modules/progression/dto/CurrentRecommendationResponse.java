package com.ironlogic.modules.progression.dto;

/**
 * DTO for the current sequence-based recommendation of one Program.
 *
 * @param programId target program id
 * @param currentBlockId block currently used for recommendation
 * @param nextSessionTemplateId next recommended session template id
 * @param sequenceCursor simple progression cursor; defaults to 0 when no ProgramProgress exists yet
 * @param recommendedSessionTemplate full recommended session template with template exercise targets
 */
public record CurrentRecommendationResponse(
        Long programId,
        Long currentBlockId,
        Long nextSessionTemplateId,
        Integer sequenceCursor,
        RecommendedSessionTemplateResponse recommendedSessionTemplate
) {
}
