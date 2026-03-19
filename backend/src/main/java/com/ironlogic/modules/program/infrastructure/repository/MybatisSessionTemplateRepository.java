package com.ironlogic.modules.program.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.program.domain.model.SessionTemplate;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.program.infrastructure.persistence.entity.SessionTemplateEntity;
import com.ironlogic.modules.program.infrastructure.persistence.mapper.SessionTemplateMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of SessionTemplateRepository.
 */
@Repository
public class MybatisSessionTemplateRepository implements SessionTemplateRepository {

    private final SessionTemplateMapper sessionTemplateMapper;

    public MybatisSessionTemplateRepository(SessionTemplateMapper sessionTemplateMapper) {
        this.sessionTemplateMapper = sessionTemplateMapper;
    }

    @Override
    public SessionTemplate save(SessionTemplate template) {
        SessionTemplateEntity entity = toEntity(template);
        sessionTemplateMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public SessionTemplate update(SessionTemplate template) {
        SessionTemplateEntity entity = toEntity(template);
        sessionTemplateMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<SessionTemplate> findById(Long id) {
        return Optional.ofNullable(sessionTemplateMapper.selectById(id)).map(MybatisSessionTemplateRepository::toDomain);
    }

    @Override
    public List<SessionTemplate> findByBlockId(Long blockId) {
        LambdaQueryWrapper<SessionTemplateEntity> wrapper = new LambdaQueryWrapper<SessionTemplateEntity>()
                .eq(SessionTemplateEntity::getBlockId, blockId)
                .orderByAsc(SessionTemplateEntity::getSequenceNo)
                .orderByAsc(SessionTemplateEntity::getId);
        return sessionTemplateMapper.selectList(wrapper).stream().map(MybatisSessionTemplateRepository::toDomain).toList();
    }

    @Override
    public boolean existsByBlockIdAndSequenceNo(Long blockId, Integer sequenceNo) {
        LambdaQueryWrapper<SessionTemplateEntity> wrapper = new LambdaQueryWrapper<SessionTemplateEntity>()
                .eq(SessionTemplateEntity::getBlockId, blockId)
                .eq(SessionTemplateEntity::getSequenceNo, sequenceNo);
        return sessionTemplateMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByBlockIdAndSequenceNoAndIdNot(Long blockId, Integer sequenceNo, Long excludeId) {
        LambdaQueryWrapper<SessionTemplateEntity> wrapper = new LambdaQueryWrapper<SessionTemplateEntity>()
                .eq(SessionTemplateEntity::getBlockId, blockId)
                .eq(SessionTemplateEntity::getSequenceNo, sequenceNo)
                .ne(SessionTemplateEntity::getId, excludeId);
        return sessionTemplateMapper.selectCount(wrapper) > 0;
    }

    private static SessionTemplateEntity toEntity(SessionTemplate template) {
        SessionTemplateEntity entity = new SessionTemplateEntity();
        entity.setId(template.id());
        entity.setBlockId(template.blockId());
        entity.setName(template.name());
        entity.setSequenceNo(template.sequenceNo());
        entity.setTriggerMode(template.triggerMode());
        entity.setNotes(template.notes());
        entity.setMetadataJson(template.metadataJson());
        entity.setCreatedAt(template.createdAt());
        entity.setUpdatedAt(template.updatedAt());
        return entity;
    }

    private static SessionTemplate toDomain(SessionTemplateEntity entity) {
        return new SessionTemplate(
                entity.getId(),
                entity.getBlockId(),
                entity.getName(),
                entity.getSequenceNo(),
                entity.getTriggerMode(),
                entity.getNotes(),
                entity.getMetadataJson(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
