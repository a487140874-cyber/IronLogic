package com.ironlogic.modules.program.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.program.application.ProgramApplicationService;
import com.ironlogic.modules.program.dto.CreateProgramRequest;
import com.ironlogic.modules.program.dto.ProgramResponse;
import com.ironlogic.modules.program.dto.UpdateProgramRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for Program APIs.
 *
 * <p>The controller only handles transport concerns. Ownership checks and template hierarchy
 * rules stay in the application service.
 */
@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramApplicationService programApplicationService;

    public ProgramController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** Lists Programs of the current user. */
    @GetMapping
    public ApiResponse<List<ProgramResponse>> listPrograms() {
        return ApiResponse.success(programApplicationService.listPrograms(currentUserId()));
    }

    /** Loads Program detail. */
    @GetMapping("/{id}")
    public ApiResponse<ProgramResponse> getProgram(@PathVariable Long id) {
        return ApiResponse.success(programApplicationService.getProgram(currentUserId(), id));
    }

    /** Creates a Program for the current user. */
    @PostMapping
    public ApiResponse<ProgramResponse> createProgram(@Valid @RequestBody CreateProgramRequest request) {
        return ApiResponse.success(programApplicationService.createProgram(currentUserId(), request));
    }

    /** Updates one owned Program. */
    @PutMapping("/{id}")
    public ApiResponse<ProgramResponse> updateProgram(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProgramRequest request
    ) {
        return ApiResponse.success(programApplicationService.updateProgram(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
