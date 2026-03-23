package com.ironlogic.modules.progression.application;

import com.ironlogic.modules.progression.dto.CurrentRecommendationResponse;
import com.ironlogic.modules.progression.dto.ProgramProgressResponse;

/**
 * Application service for progression module use cases.
 *
 * <p>This layer owns sequence-based recommendation state. Workout execution remains in the
 * workout module, and progression only reacts after a workout has already been completed.
 */
public interface ProgressionApplicationService {

    /**
     * Returns the current recommended SessionTemplate for one Program.
     *
     * @param userId current user id
     * @param programId target program id
     * @return current recommendation response
     */
    CurrentRecommendationResponse getCurrentRecommendation(Long userId, Long programId);

    /**
     * Returns the persisted ProgramProgress row for one Program when it exists.
     *
     * @param userId current user id
     * @param programId target program id
     * @return persisted progression state, or {@code null} when progression has not been initialized yet
     */
    ProgramProgressResponse getProgramProgress(Long userId, Long programId);

    /**
     * Advances ProgramProgress after one template workout has been completed.
     *
     * @param userId current user id
     * @param workoutId completed workout id
     * @param programId source program id of the completed workout
     * @param blockId source block id of the completed workout
     * @param templateId source session template id of the completed workout
     */
    void advanceProgramProgressAfterWorkoutCompletion(
            Long userId,
            Long workoutId,
            Long programId,
            Long blockId,
            Long templateId
    );
}
