package com.ironlogic.modules.exercise.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.exercise.application.ExerciseApplicationService;
import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP entrypoint for Exercise module APIs.
 *
 * <p>This controller intentionally stays thin. It only receives HTTP parameters, triggers
 * bean validation, supplies the temporary current user id, and delegates real use-case work
 * to {@link ExerciseApplicationService}. Keeping the controller small makes the business flow
 * easier to test in the application layer.
 */
@Validated
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseApplicationService exerciseApplicationService;

    public ExerciseController(ExerciseApplicationService exerciseApplicationService) {
        this.exerciseApplicationService = exerciseApplicationService;
    }

    /**
     * Lists visible exercises for the current user.
     *
     * @param request optional query filters such as exercise name
     * @return system exercises plus the current user's custom exercises
     */
    @GetMapping
    public ApiResponse<List<ExerciseResponse>> listExercises(@Valid @ModelAttribute ExerciseQueryRequest request) {
        return ApiResponse.success(exerciseApplicationService.listExercises(currentUserId(), request));
    }

    /**
     * Loads exercise detail.
     *
     * @param id exercise id
     * @return the visible exercise detail
     */
    @GetMapping("/{id}")
    public ApiResponse<ExerciseResponse> getExercise(@PathVariable Long id) {
        return ApiResponse.success(exerciseApplicationService.getExercise(currentUserId(), id));
    }

    /**
     * Creates a new custom exercise for the current user.
     *
     * @param request create payload
     * @return created exercise
     */
    @PostMapping
    public ApiResponse<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return ApiResponse.success(exerciseApplicationService.createCustomExercise(currentUserId(), request));
    }

    /**
     * Updates an existing custom exercise.
     *
     * @param id exercise id
     * @param request update payload
     * @return updated exercise
     */
    @PutMapping("/{id}")
    public ApiResponse<ExerciseResponse> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExerciseRequest request
    ) {
        return ApiResponse.success(exerciseApplicationService.updateCustomExercise(currentUserId(), id, request));
    }

    /**
     * Provides the MVP current user id.
     *
     * <p>Authentication is intentionally postponed in this round, so a fixed id is used to let
     * the rest of the exercise module close the loop end to end.
     *
     * @return temporary current user id
     */
    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
