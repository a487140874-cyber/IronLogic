package com.ironlogic.modules.progression.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.progression.domain.model.ProgramProgress;
import com.ironlogic.modules.progression.domain.repository.ProgramProgressRepository;
import com.ironlogic.modules.progression.infrastructure.persistence.entity.ProgramProgressEntity;
import com.ironlogic.modules.progression.infrastructure.persistence.mapper.ProgramProgressMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of ProgramProgressRepository.
 */
@Repository
public class MybatisProgramProgressRepository implements ProgramProgressRepository {

    private final ProgramProgressMapper programProgressMapper;

    public MybatisProgramProgressRepository(ProgramProgressMapper programProgressMapper) {
        this.programProgressMapper = programProgressMapper;
    }

    @Override
    public ProgramProgress save(ProgramProgress programProgress) {
        ProgramProgressEntity entity = toEntity(programProgress);
        programProgressMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public ProgramProgress update(ProgramProgress programProgress) {
        ProgramProgressEntity entity = toEntity(programProgress);
        programProgressMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<ProgramProgress> findByUserIdAndProgramId(Long userId, Long programId) {
        LambdaQueryWrapper<ProgramProgressEntity> wrapper = new LambdaQueryWrapper<ProgramProgressEntity>()
                .eq(ProgramProgressEntity::getUserId, userId)
                .eq(ProgramProgressEntity::getProgramId, programId);
        return Optional.ofNullable(programProgressMapper.selectOne(wrapper)).map(MybatisProgramProgressRepository::toDomain);
    }

    private static ProgramProgressEntity toEntity(ProgramProgress programProgress) {
        ProgramProgressEntity entity = new ProgramProgressEntity();
        entity.setId(programProgress.id());
        entity.setUserId(programProgress.userId());
        entity.setProgramId(programProgress.programId());
        entity.setCurrentBlockId(programProgress.currentBlockId());
        entity.setNextSessionTemplateId(programProgress.nextSessionTemplateId());
        entity.setLastCompletedWorkoutId(programProgress.lastCompletedWorkoutId());
        entity.setSequenceCursor(programProgress.sequenceCursor());
        entity.setProgressSnapshotJson(programProgress.progressSnapshotJson());
        entity.setUpdatedAt(programProgress.updatedAt());
        return entity;
    }

    private static ProgramProgress toDomain(ProgramProgressEntity entity) {
        return new ProgramProgress(
                entity.getId(),
                entity.getUserId(),
                entity.getProgramId(),
                entity.getCurrentBlockId(),
                entity.getNextSessionTemplateId(),
                entity.getLastCompletedWorkoutId(),
                entity.getSequenceCursor(),
                entity.getProgressSnapshotJson(),
                entity.getUpdatedAt()
        );
    }
}
