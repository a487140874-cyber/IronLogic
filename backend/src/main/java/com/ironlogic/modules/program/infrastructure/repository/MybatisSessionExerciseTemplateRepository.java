package com.ironlogic.modules.program.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.infrastructure.persistence.entity.SessionExerciseTemplateEntity;
import com.ironlogic.modules.program.infrastructure.persistence.mapper.SessionExerciseTemplateMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus implementation of SessionExerciseTemplateRepository.
 */
@Repository
public class MybatisSessionExerciseTemplateRepository implements SessionExerciseTemplateRepository {

    private final SessionExerciseTemplateMapper sessionExerciseTemplateMapper;

    public MybatisSessionExerciseTemplateRepository(SessionExerciseTemplateMapper sessionExerciseTemplateMapper) {
        this.sessionExerciseTemplateMapper = sessionExerciseTemplateMapper;
    }

    @Override
    public SessionExerciseTemplate save(SessionExerciseTemplate templateExercise) {
        SessionExerciseTemplateEntity entity = toEntity(templateExercise);
        sessionExerciseTemplateMapper.insert(entity);
        return toDomain(entity);
    }

    @Override
    public SessionExerciseTemplate update(SessionExerciseTemplate templateExercise) {
        SessionExerciseTemplateEntity entity = toEntity(templateExercise);
        sessionExerciseTemplateMapper.updateById(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<SessionExerciseTemplate> findById(Long id) {
        return Optional.ofNullable(sessionExerciseTemplateMapper.selectById(id))
                .map(MybatisSessionExerciseTemplateRepository::toDomain);
    }

    @Override
    public List<SessionExerciseTemplate> findBySessionTemplateId(Long sessionTemplateId) {
        LambdaQueryWrapper<SessionExerciseTemplateEntity> wrapper = new LambdaQueryWrapper<SessionExerciseTemplateEntity>()
                .eq(SessionExerciseTemplateEntity::getSessionTemplateId, sessionTemplateId)
                .orderByAsc(SessionExerciseTemplateEntity::getOrderNo)
                .orderByAsc(SessionExerciseTemplateEntity::getId);
        return sessionExerciseTemplateMapper.selectList(wrapper).stream()
                .map(MybatisSessionExerciseTemplateRepository::toDomain)
                .toList();
    }

    @Override
    public boolean existsBySessionTemplateIdAndOrderNo(Long sessionTemplateId, Integer orderNo) {
        LambdaQueryWrapper<SessionExerciseTemplateEntity> wrapper = new LambdaQueryWrapper<SessionExerciseTemplateEntity>()
                .eq(SessionExerciseTemplateEntity::getSessionTemplateId, sessionTemplateId)
                .eq(SessionExerciseTemplateEntity::getOrderNo, orderNo);
        return sessionExerciseTemplateMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsBySessionTemplateIdAndOrderNoAndIdNot(Long sessionTemplateId, Integer orderNo, Long excludeId) {
        LambdaQueryWrapper<SessionExerciseTemplateEntity> wrapper = new LambdaQueryWrapper<SessionExerciseTemplateEntity>()
                .eq(SessionExerciseTemplateEntity::getSessionTemplateId, sessionTemplateId)
                .eq(SessionExerciseTemplateEntity::getOrderNo, orderNo)
                .ne(SessionExerciseTemplateEntity::getId, excludeId);
        return sessionExerciseTemplateMapper.selectCount(wrapper) > 0;
    }

    private static SessionExerciseTemplateEntity toEntity(SessionExerciseTemplate templateExercise) {
        SessionExerciseTemplateEntity entity = new SessionExerciseTemplateEntity();
        entity.setId(templateExercise.id());
        entity.setSessionTemplateId(templateExercise.sessionTemplateId());
        entity.setExerciseId(templateExercise.exerciseId());
        entity.setOrderNo(templateExercise.orderNo());
        entity.setTargetSets(templateExercise.targetSets());
        entity.setTargetReps(templateExercise.targetReps());
        entity.setTargetWeight(templateExercise.targetWeight());
        entity.setTargetWeightUnit(templateExercise.targetWeightUnit());
        entity.setRestSeconds(templateExercise.restSeconds());
        entity.setIntensityMode(templateExercise.intensityMode());
        entity.setProgressionRuleId(templateExercise.progressionRuleId());
        entity.setPrescriptionJson(templateExercise.prescriptionJson());
        entity.setCreatedAt(templateExercise.createdAt());
        entity.setUpdatedAt(templateExercise.updatedAt());
        return entity;
    }

    private static SessionExerciseTemplate toDomain(SessionExerciseTemplateEntity entity) {
        return new SessionExerciseTemplate(
                entity.getId(),
                entity.getSessionTemplateId(),
                entity.getExerciseId(),
                entity.getOrderNo(),
                entity.getTargetSets(),
                entity.getTargetReps(),
                entity.getTargetWeight(),
                entity.getTargetWeightUnit(),
                entity.getRestSeconds(),
                entity.getIntensityMode(),
                entity.getProgressionRuleId(),
                entity.getPrescriptionJson(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
