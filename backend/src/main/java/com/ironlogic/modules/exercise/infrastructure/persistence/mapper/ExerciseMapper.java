package com.ironlogic.modules.exercise.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.exercise.infrastructure.persistence.entity.ExerciseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for the {@code exercises} table.
 *
 * <p>No custom XML is needed for the current MVP because BaseMapper already covers basic CRUD,
 * and repository-level query wrappers are sufficient for the visible exercise queries.
 */
@Mapper
public interface ExerciseMapper extends BaseMapper<ExerciseEntity> {
}
