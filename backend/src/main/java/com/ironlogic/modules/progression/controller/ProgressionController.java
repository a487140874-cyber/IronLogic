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
 * progression 模块的 HTTP Controller。
 *
 * <p>这一层故意保持轻量，只负责提供临时用户 id，并把序列推荐逻辑委托给 application service。
 */
@RestController
@RequestMapping("/api/progression/programs")
public class ProgressionController {

    private final ProgressionApplicationService progressionApplicationService;

    public ProgressionController(ProgressionApplicationService progressionApplicationService) {
        this.progressionApplicationService = progressionApplicationService;
    }

    /**
     * 返回某个 Program 当前基于序列的推荐结果。
     *
     * @param programId 目标 Program id
     * @return 当前推荐模板及其模板动作目标值
     */
    @GetMapping("/{programId}/current-recommendation")
    public ApiResponse<CurrentRecommendationResponse> getCurrentRecommendation(@PathVariable Long programId) {
        return ApiResponse.success(progressionApplicationService.getCurrentRecommendation(currentUserId(), programId));
    }

    /**
     * 返回某个 Program 当前已持久化的 ProgramProgress。
     *
     * @param programId 目标 Program id
     * @return 当前 ProgramProgress；如果尚未初始化则返回 {@code null}
     */
    @GetMapping("/{programId}/progress")
    public ApiResponse<ProgramProgressResponse> getProgramProgress(@PathVariable Long programId) {
        return ApiResponse.success(progressionApplicationService.getProgramProgress(currentUserId(), programId));
    }

    /**
     * 提供 MVP 阶段的当前用户 id。
     *
     * <p>认证模块暂未实现，因此这里先使用固定 id，让 progression 可以先完整闭环。
     *
     * @return 临时当前用户 id
     */
    private Long currentUserId() {
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
