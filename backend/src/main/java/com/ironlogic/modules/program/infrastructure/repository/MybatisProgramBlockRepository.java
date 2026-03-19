package com.ironlogic.modules.program.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.program.domain.model.ProgramBlock;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.infrastructure.persistence.entity.ProgramBlockEntity;
import com.ironlogic.modules.program.infrastructure.persistence.mapper.ProgramBlockMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of ProgramBlockRepository.
 */
@Repository
public class MybatisProgramBlockRepository implements ProgramBlockRepository {

    private final ProgramBlockMapper programBlockMapper;

    public MybatisProgramBlockRepository(ProgramBlockMapper programBlockMapper) {
        this.programBlockMapper = programBlockMapper;
    }

    @Override
    public ProgramBlock save(ProgramBlock block) {
        ProgramBlockEntity entity = toEntity(block);
        programBlockMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public ProgramBlock update(ProgramBlock block) {
        ProgramBlockEntity entity = toEntity(block);
        programBlockMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<ProgramBlock> findById(Long id) {
        return Optional.ofNullable(programBlockMapper.selectById(id)).map(MybatisProgramBlockRepository::toDomain);
    }

    @Override
    public List<ProgramBlock> findByProgramId(Long programId) {
        LambdaQueryWrapper<ProgramBlockEntity> wrapper = new LambdaQueryWrapper<ProgramBlockEntity>()
                .eq(ProgramBlockEntity::getProgramId, programId)
                .orderByAsc(ProgramBlockEntity::getSequenceNo)
                .orderByAsc(ProgramBlockEntity::getId);
        return programBlockMapper.selectList(wrapper).stream().map(MybatisProgramBlockRepository::toDomain).toList();
    }

    @Override
    public boolean existsByProgramIdAndSequenceNo(Long programId, Integer sequenceNo) {
        LambdaQueryWrapper<ProgramBlockEntity> wrapper = new LambdaQueryWrapper<ProgramBlockEntity>()
                .eq(ProgramBlockEntity::getProgramId, programId)
                .eq(ProgramBlockEntity::getSequenceNo, sequenceNo);
        return programBlockMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByProgramIdAndSequenceNoAndIdNot(Long programId, Integer sequenceNo, Long excludeId) {
        LambdaQueryWrapper<ProgramBlockEntity> wrapper = new LambdaQueryWrapper<ProgramBlockEntity>()
                .eq(ProgramBlockEntity::getProgramId, programId)
                .eq(ProgramBlockEntity::getSequenceNo, sequenceNo)
                .ne(ProgramBlockEntity::getId, excludeId);
        return programBlockMapper.selectCount(wrapper) > 0;
    }

    private static ProgramBlockEntity toEntity(ProgramBlock block) {
        ProgramBlockEntity entity = new ProgramBlockEntity();
        entity.setId(block.id());
        entity.setProgramId(block.programId());
        entity.setName(block.name());
        entity.setBlockType(block.blockType());
        entity.setSequenceNo(block.sequenceNo());
        entity.setDurationMode(block.durationMode());
        entity.setDurationValue(block.durationValue());
        entity.setDeloadEnabled(block.deloadEnabled());
        entity.setMetadataJson(block.metadataJson());
        entity.setCreatedAt(block.createdAt());
        entity.setUpdatedAt(block.updatedAt());
        return entity;
    }

    private static ProgramBlock toDomain(ProgramBlockEntity entity) {
        return new ProgramBlock(
                entity.getId(),
                entity.getProgramId(),
                entity.getName(),
                entity.getBlockType(),
                entity.getSequenceNo(),
                entity.getDurationMode(),
                entity.getDurationValue(),
                entity.getDeloadEnabled(),
                entity.getMetadataJson(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
