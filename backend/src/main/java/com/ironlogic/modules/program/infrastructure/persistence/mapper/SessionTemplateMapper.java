package com.ironlogic.modules.program.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ironlogic.modules.program.infrastructure.persistence.entity.SessionTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis-Plus mapper for {@code session_templates}.
 */
@Mapper
public interface SessionTemplateMapper extends BaseMapper<SessionTemplateEntity> {
}
