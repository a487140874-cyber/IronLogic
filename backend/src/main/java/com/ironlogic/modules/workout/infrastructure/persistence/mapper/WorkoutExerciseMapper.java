package com.ironlogic.modules.workout.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutExerciseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code workout_exercises}.
 */
@Mapper
public interface WorkoutExerciseMapper extends BaseMapper<WorkoutExerciseEntity> {
}
