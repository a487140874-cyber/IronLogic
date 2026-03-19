package com.ironlogic.modules.program.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.infrastructure.persistence.entity.ProgramEntity;
import com.ironlogic.modules.program.infrastructure.persistence.mapper.ProgramMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of ProgramRepository.
 */
@Repository
public class MybatisProgramRepository implements ProgramRepository {

    private final ProgramMapper programMapper;

    public MybatisProgramRepository(ProgramMapper programMapper) {
        this.programMapper = programMapper;
    }

    @Override
    public Program save(Program program) {
        ProgramEntity entity = toEntity(program);
        programMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public Program update(Program program) {
        ProgramEntity entity = toEntity(program);
        programMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Program> findById(Long id) {
        return Optional.ofNullable(programMapper.selectById(id)).map(MybatisProgramRepository::toDomain);
    }

    @Override
    public Optional<Program> findByIdAndUserId(Long id, Long userId) {
        LambdaQueryWrapper<ProgramEntity> wrapper = new LambdaQueryWrapper<ProgramEntity>()
                .eq(ProgramEntity::getId, id)
                .eq(ProgramEntity::getUserId, userId);
        return Optional.ofNullable(programMapper.selectOne(wrapper)).map(MybatisProgramRepository::toDomain);
    }

    @Override
    public List<Program> findByUserId(Long userId) {
        LambdaQueryWrapper<ProgramEntity> wrapper = new LambdaQueryWrapper<ProgramEntity>()
                .eq(ProgramEntity::getUserId, userId)
                .orderByDesc(ProgramEntity::getId);
        return programMapper.selectList(wrapper).stream().map(MybatisProgramRepository::toDomain).toList();
    }

    private static ProgramEntity toEntity(Program program) {
        ProgramEntity entity = new ProgramEntity();
        entity.setId(program.id());
        entity.setUserId(program.userId());
        entity.setName(program.name());
        entity.setGoalType(program.goalType());
        entity.setStatus(program.status());
        entity.setDescription(program.description());
        entity.setStartDate(program.startDate());
        entity.setEndDate(program.endDate());
        entity.setCreatedAt(program.createdAt());
        entity.setUpdatedAt(program.updatedAt());
        return entity;
    }

    private static Program toDomain(ProgramEntity entity) {
        return new Program(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getGoalType(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
