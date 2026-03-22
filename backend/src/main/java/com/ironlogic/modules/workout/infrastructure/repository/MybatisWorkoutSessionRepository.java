package com.ironlogic.modules.workout.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.workout.domain.model.WorkoutSession;
import com.ironlogic.modules.workout.domain.model.WorkoutSourceType;
import com.ironlogic.modules.workout.domain.model.WorkoutStatus;
import com.ironlogic.modules.workout.domain.repository.WorkoutSessionRepository;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutSessionEntity;
import com.ironlogic.modules.workout.infrastructure.persistence.mapper.WorkoutSessionMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of WorkoutSessionRepository.
 */
@Repository
public class MybatisWorkoutSessionRepository implements WorkoutSessionRepository {

    private final WorkoutSessionMapper workoutSessionMapper;

    public MybatisWorkoutSessionRepository(WorkoutSessionMapper workoutSessionMapper) {
        this.workoutSessionMapper = workoutSessionMapper;
    }

    @Override
    public WorkoutSession save(WorkoutSession workoutSession) {
        WorkoutSessionEntity entity = toEntity(workoutSession);
        workoutSessionMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public WorkoutSession update(WorkoutSession workoutSession) {
        WorkoutSessionEntity entity = toEntity(workoutSession);
        workoutSessionMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<WorkoutSession> findById(Long id) {
        return Optional.ofNullable(workoutSessionMapper.selectById(id)).map(MybatisWorkoutSessionRepository::toDomain);
    }

    @Override
    public Optional<WorkoutSession> findByIdAndUserId(Long id, Long userId) {
        LambdaQueryWrapper<WorkoutSessionEntity> wrapper = new LambdaQueryWrapper<WorkoutSessionEntity>()
                .eq(WorkoutSessionEntity::getId, id)
                .eq(WorkoutSessionEntity::getUserId, userId);
        return Optional.ofNullable(workoutSessionMapper.selectOne(wrapper)).map(MybatisWorkoutSessionRepository::toDomain);
    }

    @Override
    public List<WorkoutSession> findByUserId(Long userId) {
        LambdaQueryWrapper<WorkoutSessionEntity> wrapper = new LambdaQueryWrapper<WorkoutSessionEntity>()
                .eq(WorkoutSessionEntity::getUserId, userId)
                .orderByDesc(WorkoutSessionEntity::getStartedAt)
                .orderByDesc(WorkoutSessionEntity::getId);
        return workoutSessionMapper.selectList(wrapper).stream().map(MybatisWorkoutSessionRepository::toDomain).toList();
    }

    private static WorkoutSessionEntity toEntity(WorkoutSession workoutSession) {
        WorkoutSessionEntity entity = new WorkoutSessionEntity();
        entity.setId(workoutSession.id());
        entity.setUserId(workoutSession.userId());
        entity.setSourceType(workoutSession.sourceType().name());
        entity.setSourceProgramId(workoutSession.sourceProgramId());
        entity.setSourceBlockId(workoutSession.sourceBlockId());
        entity.setSourceTemplateId(workoutSession.sourceTemplateId());
        entity.setStatus(workoutSession.status().name());
        entity.setStartedAt(workoutSession.startedAt());
        entity.setEndedAt(workoutSession.endedAt());
        entity.setNotes(workoutSession.notes());
        entity.setCreatedAt(workoutSession.createdAt());
        entity.setUpdatedAt(workoutSession.updatedAt());
        return entity;
    }

    private static WorkoutSession toDomain(WorkoutSessionEntity entity) {
        return new WorkoutSession(
                entity.getId(),
                entity.getUserId(),
                WorkoutSourceType.valueOf(entity.getSourceType()),
                entity.getSourceProgramId(),
                entity.getSourceBlockId(),
                entity.getSourceTemplateId(),
                WorkoutStatus.valueOf(entity.getStatus()),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
