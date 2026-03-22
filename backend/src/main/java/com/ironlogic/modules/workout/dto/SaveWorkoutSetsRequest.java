package com.ironlogic.modules.workout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request DTO for the MVP set overwrite API.
 *
 * <p>MVP uses whole-list replacement because it is much easier to reason about than incremental
 * set patching while the workout execution model is still being stabilized.
 *
 * @param sets full list of sets that should become the new persisted state
 */
public record SaveWorkoutSetsRequest(
        /** Complete replacement list of sets for one workout exercise. */
        @NotEmpty
        List<@Valid WorkoutSetInput> sets
) {
}
