package com.ironlogic.modules.exercise.application;

import com.ironlogic.common.exception.ForbiddenException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.model.Exercise;
import com.ironlogic.modules.exercise.domain.policy.ExerciseOwnershipPolicy;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of Exercise application use cases.
 *
 * <p>This class is placed in the application layer because its main job is orchestration:
 * it receives DTOs from the controller, enforces module-level rules such as ownership and
 * system/custom boundaries, and then delegates data access to the repository abstraction.
 * The logic stays here instead of controller so the HTTP layer remains thin and reusable.
 */
@Service
public class ExerciseApplicationServiceImpl implements ExerciseApplicationService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseApplicationServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Creates a custom exercise owned by the current user.
     *
     * @param userId id of the current user
     * @param request incoming create payload
     * @return created exercise response
     */
    @Override
    @Transactional
    public ExerciseResponse createCustomExercise(Long userId, CreateExerciseRequest request) {
        LocalDateTime now = LocalDateTime.now();
        // A user-created exercise must always be tied to the current user and marked custom.
        // These two fields are derived from business rules, not trusted from client input.
        Exercise exercise = new Exercise(
                null,
                userId,
                normalize(request.name()),
                normalize(request.category()),
                normalize(request.primaryMuscle()),
                normalize(request.secondaryMusclesJson()),
                normalize(request.equipmentType()),
                normalize(request.movementPattern()),
                true,
                normalize(request.metadataJson()),
                now,
                now
        );
        return toResponse(exerciseRepository.save(exercise));
    }

    /**
     * Updates an existing custom exercise.
     *
     * @param userId id of the current user
     * @param exerciseId target exercise id
     * @param request incoming update payload
     * @return updated exercise response
     * @throws NotFoundException when target exercise does not exist
     * @throws ForbiddenException when the exercise is system-owned or belongs to another user
     */
    @Override
    @Transactional
    public ExerciseResponse updateCustomExercise(Long userId, Long exerciseId, UpdateExerciseRequest request) {
        Exercise existing = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));

        // System exercises are shared reference data. Allowing update here would let one user
        // mutate global catalog data, so they are explicitly read-only in MVP.
        // For custom exercises, only the owner may modify them.
        if (!ExerciseOwnershipPolicy.canModify(existing, userId)) {
            if (existing.ownerUserId() == null || !Boolean.TRUE.equals(existing.isCustom())) {
                throw new ForbiddenException("System exercise cannot be modified");
            }
            throw new ForbiddenException("You can only modify your own custom exercise");
        }

        Exercise updated = new Exercise(
                existing.id(),
                existing.ownerUserId(),
                normalize(request.name()),
                normalize(request.category()),
                normalize(request.primaryMuscle()),
                normalize(request.secondaryMusclesJson()),
                normalize(request.equipmentType()),
                normalize(request.movementPattern()),
                true,
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toResponse(exerciseRepository.update(updated));
    }

    /**
     * Returns a single exercise if it is visible to the current user.
     *
     * @param userId id of the current user
     * @param exerciseId target exercise id
     * @return exercise detail
     * @throws NotFoundException when not found or not visible
     */
    @Override
    @Transactional(readOnly = true)
    public ExerciseResponse getExercise(Long userId, Long exerciseId) {
        return exerciseRepository.findVisibleById(userId, exerciseId)
                .map(ExerciseApplicationServiceImpl::toResponse)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
    }

    /**
     * Lists visible exercises for the current user.
     *
     * @param userId id of the current user
     * @param request optional query filters
     * @return visible exercise list including system exercises and user's custom exercises
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExerciseResponse> listExercises(Long userId, ExerciseQueryRequest request) {
        return exerciseRepository.findVisible(
                        userId,
                        normalize(request.getName()),
                        normalize(request.getCategory()),
                        normalize(request.getEquipmentType())
                ).stream()
                .map(ExerciseApplicationServiceImpl::toResponse)
                .toList();
    }

    /**
     * Maps domain object to response DTO.
     *
     * @param exercise domain exercise object
     * @return response DTO used by controller layer
     */
    private static ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.id(),
                exercise.ownerUserId(),
                exercise.name(),
                exercise.category(),
                exercise.primaryMuscle(),
                exercise.secondaryMusclesJson(),
                exercise.equipmentType(),
                exercise.movementPattern(),
                exercise.isCustom(),
                exercise.metadataJson(),
                exercise.createdAt(),
                exercise.updatedAt()
        );
    }

    /**
     * Trims incoming string values and converts blank strings to {@code null}.
     *
     * <p>This keeps the stored data cleaner and avoids treating blank text as meaningful data.
     *
     * @param value raw input value
     * @return normalized value or {@code null}
     */
    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
