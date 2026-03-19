package com.ironlogic.modules.program.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.program.application.ProgramApplicationService;
import com.ironlogic.modules.program.dto.CreateSessionTemplateRequest;
import com.ironlogic.modules.program.dto.SessionTemplateResponse;
import com.ironlogic.modules.program.dto.UpdateSessionTemplateRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for SessionTemplate APIs.
 */
@RestController
public class SessionTemplateController {

    private final ProgramApplicationService programApplicationService;

    public SessionTemplateController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** Lists session templates under one owned ProgramBlock. */
    @GetMapping("/api/blocks/{blockId}/session-templates")
    public ApiResponse<List<SessionTemplateResponse>> listSessionTemplates(@PathVariable Long blockId) {
        return ApiResponse.success(programApplicationService.listSessionTemplates(currentUserId(), blockId));
    }

    /** Creates a session template under one owned ProgramBlock. */
    @PostMapping("/api/blocks/{blockId}/session-templates")
    public ApiResponse<SessionTemplateResponse> createSessionTemplate(
            @PathVariable Long blockId,
            @Valid @RequestBody CreateSessionTemplateRequest request
    ) {
        return ApiResponse.success(programApplicationService.createSessionTemplate(currentUserId(), blockId, request));
    }

    /** Updates one owned SessionTemplate. */
    @PutMapping("/api/session-templates/{id}")
    public ApiResponse<SessionTemplateResponse> updateSessionTemplate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSessionTemplateRequest request
    ) {
        return ApiResponse.success(programApplicationService.updateSessionTemplate(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
