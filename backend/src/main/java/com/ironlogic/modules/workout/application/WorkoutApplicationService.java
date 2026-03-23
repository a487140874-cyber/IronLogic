package com.ironlogic.modules.workout.application;

import com.ironlogic.modules.workout.dto.AddWorkoutExerciseRequest;
import com.ironlogic.modules.workout.dto.CreateManualWorkoutRequest;
import com.ironlogic.modules.workout.dto.SaveWorkoutSetsRequest;
import com.ironlogic.modules.workout.dto.WorkoutDetailResponse;
import com.ironlogic.modules.workout.dto.WorkoutExerciseResponse;
import com.ironlogic.modules.workout.dto.WorkoutHistoryItemResponse;
import java.util.List;

/**
 * Workout 执行层的应用服务接口。
 *
 * <p>这一层负责串联 program 模块的模板读取、exercise 模块的可见性校验，以及
 * workout 执行表的写入操作。
 */
public interface WorkoutApplicationService {

    /** 基于一个可见的 SessionTemplate 开始训练。 */
    WorkoutDetailResponse createWorkoutFromTemplate(Long userId, Long templateId);

    /** 开始一次手动自由训练。 */
    WorkoutDetailResponse createManualWorkout(Long userId, CreateManualWorkoutRequest request);

    /** 查询完整训练详情，包含动作与组。 */
    WorkoutDetailResponse getWorkoutDetail(Long userId, Long workoutId);

    /** 列出当前用户的训练历史。 */
    List<WorkoutHistoryItemResponse> listWorkoutHistory(Long userId);

    /** 向进行中的训练中新增一个动作。 */
    WorkoutExerciseResponse addWorkoutExercise(Long userId, Long workoutId, AddWorkoutExerciseRequest request);

    /** 覆盖保存某个训练动作下的所有组。 */
    List<com.ironlogic.modules.workout.dto.WorkoutSetResponse> saveWorkoutSets(
            Long userId,
            Long workoutExerciseId,
            SaveWorkoutSetsRequest request
    );

    /** 完成一次进行中的训练。 */
    WorkoutDetailResponse finishWorkout(Long userId, Long workoutId);
}
