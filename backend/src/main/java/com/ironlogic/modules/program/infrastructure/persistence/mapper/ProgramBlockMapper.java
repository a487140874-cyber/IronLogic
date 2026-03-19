package com.ironlogic.modules.program.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.program.infrastructure.persistence.entity.ProgramBlockEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code program_blocks}.
 */
@Mapper
public interface ProgramBlockMapper extends BaseMapper<ProgramBlockEntity> {
}
