package com.ironlogic.modules.program.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.program.infrastructure.persistence.entity.ProgramEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code programs}.
 */
@Mapper
public interface ProgramMapper extends BaseMapper<ProgramEntity> {
}
