package com.ironlogic.modules.exercise.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ironlogic.modules.exercise.domain.model.Exercise;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.exercise.infrastructure.persistence.entity.ExerciseEntity;
import com.ironlogic.modules.exercise.infrastructure.persistence.mapper.ExerciseMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MyBatis-Plus based repository implementation for Exercise module.
 *
 * <p>This class stays in infrastructure because it knows about query wrappers, mapper APIs,
 * and the persistence entity. It translates those details into the domain-facing repository
 * interface used by the application layer.
 */
@Repository
public class MybatisExerciseRepository implements ExerciseRepository {

    private final ExerciseMapper exerciseMapper;

    public MybatisExerciseRepository(ExerciseMapper exerciseMapper) {
        this.exerciseMapper = exerciseMapper;
    }

    /**
     * Inserts a new exercise row.
     *
     * @param exercise domain exercise to persist
     * @return persisted exercise with generated id
     */
    @Override
    public Exercise save(Exercise exercise) {
        ExerciseEntity entity = toEntity(exercise);
        exerciseMapper.insert(entity);
        return toDomain(entity);
    }

    /**
     * Updates an existing exercise row by id.
     *
     * @param exercise updated domain exercise
     * @return updated exercise
     */
    @Override
    public Exercise update(Exercise exercise) {
        ExerciseEntity entity = toEntity(exercise);
        exerciseMapper.updateById(entity);
        return toDomain(entity);
    }

    /**
     * Loads an exercise by id without applying visibility rules.
     *
     * @param id exercise id
     * @return optional exercise
     */
    @Override
    public Optional<Exercise> findById(Long id) {
        return Optional.ofNullable(exerciseMapper.selectById(id)).map(MybatisExerciseRepository::toDomain);
    }

    /**
     * Loads an exercise only if it is visible to the current user.
     *
     * @param userId current user id
     * @param id exercise id
     * @return optional visible exercise
     */
    @Override
    public Optional<Exercise> findVisibleById(Long userId, Long id) {
        LambdaQueryWrapper<ExerciseEntity> wrapper = new LambdaQueryWrapper<ExerciseEntity>()
                .eq(ExerciseEntity::getId, id)
                // A visible exercise is either a system exercise (owner_user_id is null)
                // or a custom exercise owned by the current user.
                .and(query -> query.isNull(ExerciseEntity::getOwnerUserId)
                        .or()
                        .eq(ExerciseEntity::getOwnerUserId, userId));
        return Optional.ofNullable(exerciseMapper.selectOne(wrapper)).map(MybatisExerciseRepository::toDomain);
    }

    /**
     * Lists all exercises visible to the current user with optional lightweight filters.
     *
     * @param userId current user id
     * @param name optional fuzzy name filter
     * @param category optional exact category filter
     * @param equipmentType optional exact equipment type filter
     * @return visible exercise list
     */
    @Override
    public List<Exercise> findVisible(Long userId, String name, String category, String equipmentType) {
        LambdaQueryWrapper<ExerciseEntity> wrapper = new LambdaQueryWrapper<ExerciseEntity>()
                // The list API must merge global catalog data with the current user's custom data.
                // This is why the query explicitly includes both owner_user_id is null and owner_user_id = userId.
                .and(query -> query.isNull(ExerciseEntity::getOwnerUserId)
                        .or()
                        .eq(ExerciseEntity::getOwnerUserId, userId))
                .like(name != null, ExerciseEntity::getName, name)
                .eq(category != null, ExerciseEntity::getCategory, category)
                .eq(equipmentType != null, ExerciseEntity::getEquipmentType, equipmentType)
                .orderByAsc(ExerciseEntity::getOwnerUserId)
                .orderByAsc(ExerciseEntity::getName)
                .orderByDesc(ExerciseEntity::getId);
        return exerciseMapper.selectList(wrapper).stream()
                .map(MybatisExerciseRepository::toDomain)
                .toList();
    }

    /**
     * Converts domain object into persistence entity.
     *
     * @param exercise domain exercise
     * @return persistence entity
     */
    private static ExerciseEntity toEntity(Exercise exercise) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.setId(exercise.id());
        entity.setOwnerUserId(exercise.ownerUserId());
        entity.setName(exercise.name());
        entity.setCategory(exercise.category());
        entity.setPrimaryMuscle(exercise.primaryMuscle());
        entity.setSecondaryMusclesJson(exercise.secondaryMusclesJson());
        entity.setEquipmentType(exercise.equipmentType());
        entity.setMovementPattern(exercise.movementPattern());
        entity.setIsCustom(exercise.isCustom());
        entity.setMetadataJson(exercise.metadataJson());
        entity.setCreatedAt(exercise.createdAt());
        entity.setUpdatedAt(exercise.updatedAt());
        return entity;
    }

    /**
     * Converts persistence entity into domain object.
     *
     * @param entity persistence entity
     * @return domain exercise
     */
    private static Exercise toDomain(ExerciseEntity entity) {
        return new Exercise(
                entity.getId(),
                entity.getOwnerUserId(),
                entity.getName(),
                entity.getCategory(),
                entity.getPrimaryMuscle(),
                entity.getSecondaryMusclesJson(),
                entity.getEquipmentType(),
                entity.getMovementPattern(),
                entity.getIsCustom(),
                entity.getMetadataJson(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
