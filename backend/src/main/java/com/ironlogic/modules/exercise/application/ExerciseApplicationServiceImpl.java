package com.ironlogic.modules.exercise.application;

import com.ironlogic.common.exception.ForbiddenException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.model.Exercise;
import com.ironlogic.modules.exercise.domain.policy.ExerciseOwnershipPolicy;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Exercise 模块应用服务的默认实现。
 *
 * <p>这个类放在 application 层，是因为它的主要职责是编排：接收 Controller 传入的 DTO，
 * 执行归属权、系统/自定义边界等模块级规则，再委托 Repository 访问数据。这样可以让
 * HTTP 层保持轻量，也便于后续直接在 application 层做单元测试。
 */
@Service
public class ExerciseApplicationServiceImpl implements ExerciseApplicationService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseApplicationServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * 为当前用户创建一个自定义 Exercise。
     *
     * @param userId 当前用户 id
     * @param request 创建请求
     * @return 创建后的 Exercise 响应
     */
    @Override
    @Transactional
    public ExerciseResponse createCustomExercise(Long userId, CreateExerciseRequest request) {
        LocalDateTime now = LocalDateTime.now();
        // 用户创建的 Exercise 必须绑定到当前用户，并显式标记为自定义。
        // 这两个字段由业务规则决定，不能信任客户端直接传入。
        Exercise exercise = new Exercise(
                null,
                userId,
                normalize(request.name()),
                normalize(request.category()),
                normalize(request.primaryMuscle()),
                normalize(request.secondaryMusclesJson()),
                normalize(request.equipmentType()),
                normalize(request.movementPattern()),
                true,
                normalize(request.metadataJson()),
                now,
                now
        );
        return toResponse(exerciseRepository.save(exercise));
    }

    /**
     * 更新一个已有的自定义 Exercise。
     *
     * @param userId 当前用户 id
     * @param exerciseId 目标 Exercise id
     * @param request 更新请求
     * @return 更新后的 Exercise 响应
     * @throws NotFoundException 目标不存在时抛出
     * @throws ForbiddenException 目标属于系统 Exercise 或不属于当前用户时抛出
     */
    @Override
    @Transactional
    public ExerciseResponse updateCustomExercise(Long userId, Long exerciseId, UpdateExerciseRequest request) {
        Exercise existing = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));

        // 系统 Exercise 属于共享参考数据。
        // 如果允许在这里更新，就会让某个用户修改全局动作目录，因此 MVP 阶段明确只读。
        // 对于自定义 Exercise，则只允许所有者修改。
        if (!ExerciseOwnershipPolicy.canModify(existing, userId)) {
            if (existing.ownerUserId() == null || !Boolean.TRUE.equals(existing.isCustom())) {
                throw new ForbiddenException("System exercise cannot be modified");
            }
            throw new ForbiddenException("You can only modify your own custom exercise");
        }

        Exercise updated = new Exercise(
                existing.id(),
                existing.ownerUserId(),
                normalize(request.name()),
                normalize(request.category()),
                normalize(request.primaryMuscle()),
                normalize(request.secondaryMusclesJson()),
                normalize(request.equipmentType()),
                normalize(request.movementPattern()),
                true,
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toResponse(exerciseRepository.update(updated));
    }

    /**
     * 返回当前用户可见的单个 Exercise。
     *
     * @param userId 当前用户 id
     * @param exerciseId 目标 Exercise id
     * @return Exercise 详情
     * @throws NotFoundException 目标不存在或不可见时抛出
     */
    @Override
    @Transactional(readOnly = true)
    public ExerciseResponse getExercise(Long userId, Long exerciseId) {
        return exerciseRepository.findVisibleById(userId, exerciseId)
                .map(ExerciseApplicationServiceImpl::toResponse)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
    }

    /**
     * 列出当前用户可见的 Exercise。
     *
     * @param userId 当前用户 id
     * @param request 可选过滤条件
     * @return 可见 Exercise 列表，包含系统 Exercise 和用户自定义 Exercise
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExerciseResponse> listExercises(Long userId, ExerciseQueryRequest request) {
        return exerciseRepository.findVisible(
                        userId,
                        normalize(request.getName()),
                        normalize(request.getCategory()),
                        normalize(request.getEquipmentType())
                ).stream()
                .map(ExerciseApplicationServiceImpl::toResponse)
                .toList();
    }

    /**
     * 将领域对象转换为响应 DTO。
     *
     * @param exercise 领域对象
     * @return Controller 层返回用的 DTO
     */
    private static ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.id(),
                exercise.ownerUserId(),
                exercise.name(),
                exercise.category(),
                exercise.primaryMuscle(),
                exercise.secondaryMusclesJson(),
                exercise.equipmentType(),
                exercise.movementPattern(),
                exercise.isCustom(),
                exercise.metadataJson(),
                exercise.createdAt(),
                exercise.updatedAt()
        );
    }

    /**
     * 去除字符串两端空白，并把空串转换为 {@code null}。
     *
     * <p>这样可以让存储数据更干净，避免把空白文本误当成有效业务值。
     *
     * @param value 原始输入
     * @return 归一化后的值；如果为空则返回 {@code null}
     */
    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
