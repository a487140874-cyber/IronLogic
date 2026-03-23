package com.ironlogic.modules.progression.dto;

import java.math.BigDecimal;

/**
 * DTO for one recommended SessionExerciseTemplate item.
 *
 * <p>This response only exposes template target values. It does not calculate real training
 * load yet because progression v1 only solves sequence-based recommendation, not load advice.
 *
 * @param id session exercise template id
 * @param exerciseId referenced exercise id
 * @param exerciseName display name of the referenced exercise
 * @param orderNo order inside the recommended session template
 * @param targetSets planned set count
 * @param targetReps planned rep target
 * @param targetWeight planned weight target from template
 * @param targetWeightUnit weight unit of the template target
 * @param restSeconds planned rest seconds
 * @param intensityMode template intensity mode
 * @param prescriptionJson extensible template prescription json
 */
public record RecommendedExerciseResponse(
        Long id,
        Long exerciseId,
        String exerciseName,
        Integer orderNo,
        Integer targetSets,
        Integer targetReps,
        BigDecimal targetWeight,
        String targetWeightUnit,
        Integer restSeconds,
        String intensityMode,
        String prescriptionJson
) {
}
