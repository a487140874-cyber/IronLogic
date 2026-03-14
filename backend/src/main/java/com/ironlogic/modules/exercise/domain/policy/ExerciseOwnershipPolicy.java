package com.ironlogic.modules.exercise.domain.policy;

import com.ironlogic.modules.exercise.domain.model.Exercise;

/**
 * Small domain policy for exercise ownership semantics.
 *
 * <p>The policy is extracted to make the ownership rules explicit and reusable. The logic is
 * simple today, but this placement keeps it out of controller code and makes the rules easier
 * to test and reason about.
 */
public final class ExerciseOwnershipPolicy {

    private ExerciseOwnershipPolicy() {
    }

    /**
     * Determines whether a user may modify an exercise.
     *
     * @param exercise target exercise
     * @param userId current user id
     * @return {@code true} only when the exercise is custom and owned by the same user
     */
    public static boolean canModify(Exercise exercise, Long userId) {
        return Boolean.TRUE.equals(exercise.isCustom()) && userId != null && userId.equals(exercise.ownerUserId());
    }

    /**
     * Determines whether a user may view an exercise.
     *
     * @param exercise target exercise
     * @param userId current user id
     * @return {@code true} for system exercises and for exercises owned by the same user
     */
    public static boolean canView(Exercise exercise, Long userId) {
        return exercise.ownerUserId() == null || (userId != null && userId.equals(exercise.ownerUserId()));
    }
}
