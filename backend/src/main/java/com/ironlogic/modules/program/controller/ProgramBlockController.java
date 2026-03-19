package com.ironlogic.modules.program.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.program.application.ProgramApplicationService;
import com.ironlogic.modules.program.dto.CreateProgramBlockRequest;
import com.ironlogic.modules.program.dto.ProgramBlockResponse;
import com.ironlogic.modules.program.dto.UpdateProgramBlockRequest;
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
 * HTTP controller for ProgramBlock APIs.
 */
@RestController
public class ProgramBlockController {

    private final ProgramApplicationService programApplicationService;

    public ProgramBlockController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** Lists blocks under one owned Program. */
    @GetMapping("/api/programs/{programId}/blocks")
    public ApiResponse<List<ProgramBlockResponse>> listProgramBlocks(@PathVariable Long programId) {
        return ApiResponse.success(programApplicationService.listProgramBlocks(currentUserId(), programId));
    }

    /** Creates a block under one owned Program. */
    @PostMapping("/api/programs/{programId}/blocks")
    public ApiResponse<ProgramBlockResponse> createProgramBlock(
            @PathVariable Long programId,
            @Valid @RequestBody CreateProgramBlockRequest request
    ) {
        return ApiResponse.success(programApplicationService.createProgramBlock(currentUserId(), programId, request));
    }

    /** Updates one owned ProgramBlock. */
    @PutMapping("/api/blocks/{id}")
    public ApiResponse<ProgramBlockResponse> updateProgramBlock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProgramBlockRequest request
    ) {
        return ApiResponse.success(programApplicationService.updateProgramBlock(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
