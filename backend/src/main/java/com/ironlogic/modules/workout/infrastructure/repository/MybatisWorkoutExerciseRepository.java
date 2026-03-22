package com.ironlogic.modules.workout.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.workout.domain.model.WorkoutExercise;
import com.ironlogic.modules.workout.domain.repository.WorkoutExerciseRepository;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutExerciseEntity;
import com.ironlogic.modules.workout.infrastructure.persistence.mapper.WorkoutExerciseMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of WorkoutExerciseRepository.
 */
@Repository
public class MybatisWorkoutExerciseRepository implements WorkoutExerciseRepository {

    private final WorkoutExerciseMapper workoutExerciseMapper;

    public MybatisWorkoutExerciseRepository(WorkoutExerciseMapper workoutExerciseMapper) {
        this.workoutExerciseMapper = workoutExerciseMapper;
    }

    @Override
    public WorkoutExercise save(WorkoutExercise workoutExercise) {
        WorkoutExerciseEntity entity = toEntity(workoutExercise);
        workoutExerciseMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<WorkoutExercise> findById(Long id) {
        return Optional.ofNullable(workoutExerciseMapper.selectById(id)).map(MybatisWorkoutExerciseRepository::toDomain);
    }

    @Override
    public List<WorkoutExercise> findByWorkoutSessionId(Long workoutSessionId) {
        LambdaQueryWrapper<WorkoutExerciseEntity> wrapper = new LambdaQueryWrapper<WorkoutExerciseEntity>()
                .eq(WorkoutExerciseEntity::getWorkoutSessionId, workoutSessionId)
                .orderByAsc(WorkoutExerciseEntity::getActualOrderNo)
                .orderByAsc(WorkoutExerciseEntity::getId);
        return workoutExerciseMapper.selectList(wrapper).stream().map(MybatisWorkoutExerciseRepository::toDomain).toList();
    }

    @Override
    public boolean existsByWorkoutSessionIdAndActualOrderNo(Long workoutSessionId, Integer actualOrderNo) {
        LambdaQueryWrapper<WorkoutExerciseEntity> wrapper = new LambdaQueryWrapper<WorkoutExerciseEntity>()
                .eq(WorkoutExerciseEntity::getWorkoutSessionId, workoutSessionId)
                .eq(WorkoutExerciseEntity::getActualOrderNo, actualOrderNo);
        return workoutExerciseMapper.selectCount(wrapper) > 0;
    }

    private static WorkoutExerciseEntity toEntity(WorkoutExercise workoutExercise) {
        WorkoutExerciseEntity entity = new WorkoutExerciseEntity();
        entity.setId(workoutExercise.id());
        entity.setWorkoutSessionId(workoutExercise.workoutSessionId());
        entity.setExerciseId(workoutExercise.exerciseId());
        entity.setSourceTemplateExerciseId(workoutExercise.sourceTemplateExerciseId());
        entity.setActualOrderNo(workoutExercise.actualOrderNo());
        entity.setReplacementOfExerciseId(workoutExercise.replacementOfExerciseId());
        entity.setNotes(workoutExercise.notes());
        entity.setCreatedAt(workoutExercise.createdAt());
        entity.setUpdatedAt(workoutExercise.updatedAt());
        return entity;
    }

    private static WorkoutExercise toDomain(WorkoutExerciseEntity entity) {
        return new WorkoutExercise(
                entity.getId(),
                entity.getWorkoutSessionId(),
                entity.getExerciseId(),
                entity.getSourceTemplateExerciseId(),
                entity.getActualOrderNo(),
                entity.getReplacementOfExerciseId(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
