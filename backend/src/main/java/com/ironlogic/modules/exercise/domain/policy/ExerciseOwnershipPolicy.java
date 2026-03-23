package com.ironlogic.modules.exercise.domain.policy;

import com.ironlogic.modules.exercise.domain.model.Exercise;

/**
 * Exercise 归属语义的领域策略。
 *
 * <p>把这部分逻辑单独抽出来，是为了让归属权规则更显式、更可复用。虽然当前规则不复杂，
 * 但放在这里可以避免 Controller 堆积判断，也更利于单测和后续演进。
 */
public final class ExerciseOwnershipPolicy {

    private ExerciseOwnershipPolicy() {
    }

    /**
     * 判断当前用户是否可以修改某个 Exercise。
     *
     * @param exercise 目标 Exercise
     * @param userId 当前用户 id
     * @return 只有当 Exercise 是自定义且归当前用户所有时才返回 {@code true}
     */
    public static boolean canModify(Exercise exercise, Long userId) {
        return Boolean.TRUE.equals(exercise.isCustom()) && userId != null && userId.equals(exercise.ownerUserId());
    }

    /**
     * 判断当前用户是否可以查看某个 Exercise。
     *
     * @param exercise 目标 Exercise
     * @param userId 当前用户 id
     * @return 系统 Exercise 或当前用户自己的 Exercise 都返回 {@code true}
     */
    public static boolean canView(Exercise exercise, Long userId) {
        return exercise.ownerUserId() == null || (userId != null && userId.equals(exercise.ownerUserId()));
    }
}
