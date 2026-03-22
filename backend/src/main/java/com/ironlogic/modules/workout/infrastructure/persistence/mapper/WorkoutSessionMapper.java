package com.ironlogic.modules.workout.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutSessionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code workout_sessions}.
 */
@Mapper
public interface WorkoutSessionMapper extends BaseMapper<WorkoutSessionEntity> {
}
