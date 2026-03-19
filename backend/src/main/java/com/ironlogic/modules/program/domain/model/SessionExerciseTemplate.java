package com.ironlogic.modules.program.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for a SessionExerciseTemplate.
 *
 * <p>This object links a SessionTemplate to an Exercise and stores the planned prescription
 * for that slot, such as target sets, reps, weight, and rest time.
 *
 * @param id template exercise id
 * @param sessionTemplateId parent session template id
 * @param exerciseId referenced exercise id
 * @param orderNo display order within one session template
 * @param targetSets planned set count
 * @param targetReps optional target reps
 * @param targetWeight optional target weight
 * @param targetWeightUnit optional target weight unit
 * @param restSeconds optional planned rest time
 * @param intensityMode optional intensity mode
 * @param progressionRuleId optional future progression rule id
 * @param prescriptionJson extensible prescription json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record SessionExerciseTemplate(
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
