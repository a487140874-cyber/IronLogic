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
 * Program 模块的 HTTP Controller。
 *
 * <p>这一层只处理传输层问题，真正的归属校验和模板层级规则仍然放在 application service。
 */
@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramApplicationService programApplicationService;

    public ProgramController(ProgramApplicationService programApplicationService) {
        this.programApplicationService = programApplicationService;
    }

    /** 列出当前用户的 Program。 */
    @GetMapping
    public ApiResponse<List<ProgramResponse>> listPrograms() {
        return ApiResponse.success(programApplicationService.listPrograms(currentUserId()));
    }

    /** 查询 Program 详情。 */
    @GetMapping("/{id}")
    public ApiResponse<ProgramResponse> getProgram(@PathVariable Long id) {
        return ApiResponse.success(programApplicationService.getProgram(currentUserId(), id));
    }

    /** 为当前用户创建 Program。 */
    @PostMapping
    public ApiResponse<ProgramResponse> createProgram(@Valid @RequestBody CreateProgramRequest request) {
        return ApiResponse.success(programApplicationService.createProgram(currentUserId(), request));
    }

    /** 更新一个属于当前用户的 Program。 */
    @PutMapping("/{id}")
    public ApiResponse<ProgramResponse> updateProgram(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProgramRequest request
    ) {
        return ApiResponse.success(programApplicationService.updateProgram(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
