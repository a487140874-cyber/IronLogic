package com.ironlogic.modules.progression.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.progression.application.ProgressionApplicationService;
import com.ironlogic.modules.progression.dto.CurrentRecommendationResponse;
import com.ironlogic.modules.progression.dto.ProgramProgressResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for progression module APIs.
 *
 * <p>This controller is intentionally thin. It only resolves the temporary current user id
 * and delegates sequence recommendation logic to the application service.
 */
@RestController
@RequestMapping("/api/progression/programs")
public class ProgressionController {

    private final ProgressionApplicationService progressionApplicationService;

    public ProgressionController(ProgressionApplicationService progressionApplicationService) {
        this.progressionApplicationService = progressionApplicationService;
    }

    /**
     * Returns the current sequence-based recommendation of one Program.
     *
     * @param programId target program id
     * @return current recommended template and its template exercise targets
     */
    @GetMapping("/{programId}/current-recommendation")
    public ApiResponse<CurrentRecommendationResponse> getCurrentRecommendation(@PathVariable Long programId) {
        return ApiResponse.success(progressionApplicationService.getCurrentRecommendation(currentUserId(), programId));
    }

    /**
     * Returns persisted ProgramProgress when it already exists.
     *
     * @param programId target program id
     * @return current ProgramProgress row, or {@code null} when progression has not been initialized yet
     */
    @GetMapping("/{programId}/progress")
    public ApiResponse<ProgramProgressResponse> getProgramProgress(@PathVariable Long programId) {
        return ApiResponse.success(progressionApplicationService.getProgramProgress(currentUserId(), programId));
    }

    /**
     * Provides the MVP current user id.
     *
     * <p>Authentication is intentionally postponed, so a fixed id is used to let progression
     * logic close the loop end to end in this round.
     *
     * @return temporary current user id
     */
    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
