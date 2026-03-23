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
 * SessionExerciseTemplate 的 HTTP Controller。
 */
@RestController
public class SessionExerciseTemplateController {

    private final ProgramApplicationService programApplicationService;

    public SessionExerciseTemplateController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** 列出指定 SessionTemplate 下的 SessionExerciseTemplate。 */
    @GetMapping("/api/session-templates/{templateId}/exercises")
    public ApiResponse<List<SessionExerciseTemplateResponse>> listSessionExerciseTemplates(@PathVariable Long templateId) {
        return ApiResponse.success(programApplicationService.listSessionExerciseTemplates(currentUserId(), templateId));
    }

    /** 在指定 SessionTemplate 下创建 SessionExerciseTemplate。 */
    @PostMapping("/api/session-templates/{templateId}/exercises")
    public ApiResponse<SessionExerciseTemplateResponse> createSessionExerciseTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody CreateSessionExerciseTemplateRequest request
    ) {
        return ApiResponse.success(
                programApplicationService.createSessionExerciseTemplate(currentUserId(), templateId, request)
        );
    }

    /** 更新一个属于当前用户 Program 层级的 SessionExerciseTemplate。 */
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
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
