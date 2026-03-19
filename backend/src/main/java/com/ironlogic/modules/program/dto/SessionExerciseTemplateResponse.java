package com.ironlogic.modules.program.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for SessionExerciseTemplate APIs.
 *
 * @param id template exercise id
 * @param sessionTemplateId parent session template id
 * @param exerciseId referenced exercise id
 * @param orderNo order number inside session template
 * @param targetSets planned set count
 * @param targetReps planned reps
 * @param targetWeight planned weight
 * @param targetWeightUnit weight unit
 * @param restSeconds rest seconds
 * @param intensityMode intensity mode
 * @param progressionRuleId optional progression rule id
 * @param prescriptionJson prescription json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record SessionExerciseTemplateResponse(
        Long id,
        Long sessionTemplateId,
        Long exerciseId,
        Integer orderNo,
        Integer targetSets,
        Integer targetReps,
        BigDecimal targetWeight,
        String targetWeightUnit,
        Integer restSeconds,
        String intensityMode,
        Long progressionRuleId,
        String prescriptionJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
