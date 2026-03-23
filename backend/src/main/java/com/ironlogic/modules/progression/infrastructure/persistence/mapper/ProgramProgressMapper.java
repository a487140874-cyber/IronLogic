package com.ironlogic.modules.progression.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.progression.infrastructure.persistence.entity.ProgramProgressEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code program_progress}.
 */
@Mapper
public interface ProgramProgressMapper extends BaseMapper<ProgramProgressEntity> {
}
