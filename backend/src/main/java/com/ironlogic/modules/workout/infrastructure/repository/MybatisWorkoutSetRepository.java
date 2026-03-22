package com.ironlogic.modules.workout.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.workout.domain.model.WorkoutSet;
import com.ironlogic.modules.workout.domain.repository.WorkoutSetRepository;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutSetEntity;
import com.ironlogic.modules.workout.infrastructure.persistence.mapper.WorkoutSetMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of WorkoutSetRepository.
 */
@Repository
public class MybatisWorkoutSetRepository implements WorkoutSetRepository {

    private final WorkoutSetMapper workoutSetMapper;

    public MybatisWorkoutSetRepository(WorkoutSetMapper workoutSetMapper) {
        this.workoutSetMapper = workoutSetMapper;
    }

    @Override
    public WorkoutSet save(WorkoutSet workoutSet) {
        WorkoutSetEntity entity = toEntity(workoutSet);
        workoutSetMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public List<WorkoutSet> findByWorkoutExerciseId(Long workoutExerciseId) {
        LambdaQueryWrapper<WorkoutSetEntity> wrapper = new LambdaQueryWrapper<WorkoutSetEntity>()
                .eq(WorkoutSetEntity::getWorkoutExerciseId, workoutExerciseId)
                .orderByAsc(WorkoutSetEntity::getSetNo)
                .orderByAsc(WorkoutSetEntity::getId);
        return workoutSetMapper.selectList(wrapper).stream().map(MybatisWorkoutSetRepository::toDomain).toList();
    }

    @Override
    public void deleteByWorkoutExerciseId(Long workoutExerciseId) {
        LambdaQueryWrapper<WorkoutSetEntity> wrapper = new LambdaQueryWrapper<WorkoutSetEntity>()
                .eq(WorkoutSetEntity::getWorkoutExerciseId, workoutExerciseId);
        workoutSetMapper.delete(wrapper);
    }

    private static WorkoutSetEntity toEntity(WorkoutSet workoutSet) {
        WorkoutSetEntity entity = new WorkoutSetEntity();
        entity.setId(workoutSet.id());
        entity.setWorkoutExerciseId(workoutSet.workoutExerciseId());
        entity.setSetNo(workoutSet.setNo());
        entity.setWeight(workoutSet.weight());
        entity.setReps(workoutSet.reps());
        entity.setDurationSeconds(workoutSet.durationSeconds());
        entity.setRestSeconds(workoutSet.restSeconds());
        entity.setRpe(workoutSet.rpe());
        entity.setRir(workoutSet.rir());
        entity.setIsWarmup(workoutSet.isWarmup());
        entity.setIsCompleted(workoutSet.isCompleted());
        entity.setCreatedAt(workoutSet.createdAt());
        entity.setUpdatedAt(workoutSet.updatedAt());
        return entity;
    }

    private static WorkoutSet toDomain(WorkoutSetEntity entity) {
        return new WorkoutSet(
                entity.getId(),
                entity.getWorkoutExerciseId(),
                entity.getSetNo(),
                entity.getWeight(),
                entity.getReps(),
                entity.getDurationSeconds(),
                entity.getRestSeconds(),
                entity.getRpe(),
                entity.getRir(),
                entity.getIsWarmup(),
                entity.getIsCompleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
