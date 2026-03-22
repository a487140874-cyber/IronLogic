package com.ironlogic.modules.workout.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.workout.infrastructure.persistence.entity.WorkoutSetEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code workout_sets}.
 */
@Mapper
public interface WorkoutSetMapper extends BaseMapper<WorkoutSetEntity> {
}
