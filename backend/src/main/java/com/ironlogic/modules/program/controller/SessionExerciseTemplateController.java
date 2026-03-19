package com.ironlogic.modules.program.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.program.application.ProgramApplicationService;
import com.ironlogic.modules.program.dto.CreateSessionExerciseTemplateRequest;
import com.ironlogic.modules.program.dto.SessionExerciseTemplateResponse;
import com.ironlogic.modules.program.dto.UpdateSessionExerciseTemplateRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for SessionExerciseTemplate APIs.
 */
@RestController
public class SessionExerciseTemplateController {

    private final ProgramApplicationService programApplicationService;

    public SessionExerciseTemplateController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** Lists template exercises under one owned SessionTemplate. */
    @GetMapping("/api/session-templates/{templateId}/exercises")
    public ApiResponse<List<SessionExerciseTemplateResponse>> listSessionExerciseTemplates(@PathVariable Long templateId) {
        return ApiResponse.success(programApplicationService.listSessionExerciseTemplates(currentUserId(), templateId));
    }

    /** Creates a template exercise under one owned SessionTemplate. */
    @PostMapping("/api/session-templates/{templateId}/exercises")
    public ApiResponse<SessionExerciseTemplateResponse> createSessionExerciseTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody CreateSessionExerciseTemplateRequest request
    ) {
        return ApiResponse.success(
                programApplicationService.createSessionExerciseTemplate(currentUserId(), templateId, request)
        );
    }

    /** Updates one owned SessionExerciseTemplate. */
    @PutMapping("/api/session-template-exercises/{id}")
    public ApiResponse<SessionExerciseTemplateResponse> updateSessionExerciseTemplate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSessionExerciseTemplateRequest request
    ) {
        return ApiResponse.success(
                programApplicationService.updateSessionExerciseTemplate(currentUserId(), id, request)
        );
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
