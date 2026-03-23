package com.ironlogic.modules.program.application;

import com.ironlogic.common.exception.BusinessException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.model.ProgramBlock;
import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import com.ironlogic.modules.program.domain.model.SessionTemplate;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.program.dto.CreateProgramBlockRequest;
import com.ironlogic.modules.program.dto.CreateProgramRequest;
import com.ironlogic.modules.program.dto.CreateSessionExerciseTemplateRequest;
import com.ironlogic.modules.program.dto.CreateSessionTemplateRequest;
import com.ironlogic.modules.program.dto.ProgramBlockResponse;
import com.ironlogic.modules.program.dto.ProgramResponse;
import com.ironlogic.modules.program.dto.SessionExerciseTemplateResponse;
import com.ironlogic.modules.program.dto.SessionTemplateResponse;
import com.ironlogic.modules.program.dto.UpdateProgramBlockRequest;
import com.ironlogic.modules.program.dto.UpdateProgramRequest;
import com.ironlogic.modules.program.dto.UpdateSessionExerciseTemplateRequest;
import com.ironlogic.modules.program.dto.UpdateSessionTemplateRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Program 模块应用服务的默认实现。
 *
 * <p>这个类负责整个模板层级的编排：沿着父子链路校验归属关系，在正确作用域内校验
 * sequence/order 唯一性，并在创建模板动作前确认引用的 Exercise 已存在且当前用户可见。
 */
@Service
public class ProgramApplicationServiceImpl implements ProgramApplicationService {

    private final ProgramRepository programRepository;
    private final ProgramBlockRepository programBlockRepository;
    private final SessionTemplateRepository sessionTemplateRepository;
    private final SessionExerciseTemplateRepository sessionExerciseTemplateRepository;
    private final ExerciseRepository exerciseRepository;

    public ProgramApplicationServiceImpl(
            ProgramRepository programRepository,
            ProgramBlockRepository programBlockRepository,
            SessionTemplateRepository sessionTemplateRepository,
            SessionExerciseTemplateRepository sessionExerciseTemplateRepository,
            ExerciseRepository exerciseRepository
    ) {
        this.programRepository = programRepository;
        this.programBlockRepository = programBlockRepository;
        this.sessionTemplateRepository = sessionTemplateRepository;
        this.sessionExerciseTemplateRepository = sessionExerciseTemplateRepository;
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * 为当前用户创建 Program。
     */
    @Override
    @Transactional
    public ProgramResponse createProgram(Long userId, CreateProgramRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Program program = new Program(
                null,
                userId,
                normalize(request.name()),
                normalize(request.goalType()),
                normalize(request.status()),
                normalize(request.description()),
                request.startDate(),
                request.endDate(),
                now,
                now
        );
        return toProgramResponse(programRepository.save(program));
    }

    /**
     * 列出当前用户的 Program。
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> listPrograms(Long userId) {
        return programRepository.findByUserId(userId).stream()
                .map(ProgramApplicationServiceImpl::toProgramResponse)
                .toList();
    }

    /**
     * 查询一个属于当前用户的 Program。
     */
    @Override
    @Transactional(readOnly = true)
    public ProgramResponse getProgram(Long userId, Long programId) {
        return toProgramResponse(requireOwnedProgram(userId, programId));
    }

    /**
     * 更新一个属于当前用户的 Program。
     */
    @Override
    @Transactional
    public ProgramResponse updateProgram(Long userId, Long programId, UpdateProgramRequest request) {
        Program existing = requireOwnedProgram(userId, programId);
        Program updated = new Program(
                existing.id(),
                existing.userId(),
                normalize(request.name()),
                normalize(request.goalType()),
                normalize(request.status()),
                normalize(request.description()),
                request.startDate(),
                request.endDate(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toProgramResponse(programRepository.update(updated));
    }

    /**
     * 在指定 Program 下创建 ProgramBlock。
     */
    @Override
    @Transactional
    public ProgramBlockResponse createProgramBlock(Long userId, Long programId, CreateProgramBlockRequest request) {
        requireOwnedProgram(userId, programId);

        // sequenceNo 只要求在同一个 Program 内唯一，因此校验必须限定在 programId 范围内。
        if (programBlockRepository.existsByProgramIdAndSequenceNo(programId, request.sequenceNo())) {
            throw new BusinessException("Block sequenceNo already exists in this program");
        }

        LocalDateTime now = LocalDateTime.now();
        ProgramBlock block = new ProgramBlock(
                null,
                programId,
                normalize(request.name()),
                normalize(request.blockType()),
                request.sequenceNo(),
                normalize(request.durationMode()),
                request.durationValue(),
                defaultFalse(request.deloadEnabled()),
                normalize(request.metadataJson()),
                now,
                now
        );
        return toProgramBlockResponse(programBlockRepository.save(block));
    }

    /**
     * 列出指定 Program 下的 ProgramBlock。
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProgramBlockResponse> listProgramBlocks(Long userId, Long programId) {
        requireOwnedProgram(userId, programId);
        return programBlockRepository.findByProgramId(programId).stream()
                .map(ProgramApplicationServiceImpl::toProgramBlockResponse)
                .toList();
    }

    /**
     * 更新一个属于当前用户 Program 层级的 ProgramBlock。
     */
    @Override
    @Transactional
    public ProgramBlockResponse updateProgramBlock(Long userId, Long blockId, UpdateProgramBlockRequest request) {
        ProgramBlock existing = requireOwnedBlock(userId, blockId);

        if (programBlockRepository.existsByProgramIdAndSequenceNoAndIdNot(
                existing.programId(),
                request.sequenceNo(),
                existing.id()
        )) {
            throw new BusinessException("Block sequenceNo already exists in this program");
        }

        ProgramBlock updated = new ProgramBlock(
                existing.id(),
                existing.programId(),
                normalize(request.name()),
                normalize(request.blockType()),
                request.sequenceNo(),
                normalize(request.durationMode()),
                request.durationValue(),
                defaultFalse(request.deloadEnabled()),
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toProgramBlockResponse(programBlockRepository.update(updated));
    }

    /**
     * 在指定 ProgramBlock 下创建 SessionTemplate。
     */
    @Override
    @Transactional
    public SessionTemplateResponse createSessionTemplate(Long userId, Long blockId, CreateSessionTemplateRequest request) {
        requireOwnedBlock(userId, blockId);

        // sequenceNo 只要求在同一个 Block 内唯一，因为模板顺序是按 block 局部定义的。
        if (sessionTemplateRepository.existsByBlockIdAndSequenceNo(blockId, request.sequenceNo())) {
            throw new BusinessException("SessionTemplate sequenceNo already exists in this block");
        }

        LocalDateTime now = LocalDateTime.now();
        SessionTemplate template = new SessionTemplate(
                null,
                blockId,
                normalize(request.name()),
                request.sequenceNo(),
                normalize(request.triggerMode()),
                normalize(request.notes()),
                normalize(request.metadataJson()),
                now,
                now
        );
        return toSessionTemplateResponse(sessionTemplateRepository.save(template));
    }

    /**
     * 列出指定 ProgramBlock 下的 SessionTemplate。
     */
    @Override
    @Transactional(readOnly = true)
    public List<SessionTemplateResponse> listSessionTemplates(Long userId, Long blockId) {
        requireOwnedBlock(userId, blockId);
        return sessionTemplateRepository.findByBlockId(blockId).stream()
                .map(ProgramApplicationServiceImpl::toSessionTemplateResponse)
                .toList();
    }

    /**
     * 更新一个属于当前用户 Program 层级的 SessionTemplate。
     */
    @Override
    @Transactional
    public SessionTemplateResponse updateSessionTemplate(Long userId, Long templateId, UpdateSessionTemplateRequest request) {
        SessionTemplate existing = requireOwnedSessionTemplate(userId, templateId);

        if (sessionTemplateRepository.existsByBlockIdAndSequenceNoAndIdNot(
                existing.blockId(),
                request.sequenceNo(),
                existing.id()
        )) {
            throw new BusinessException("SessionTemplate sequenceNo already exists in this block");
        }

        SessionTemplate updated = new SessionTemplate(
                existing.id(),
                existing.blockId(),
                normalize(request.name()),
                request.sequenceNo(),
                normalize(request.triggerMode()),
                normalize(request.notes()),
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toSessionTemplateResponse(sessionTemplateRepository.update(updated));
    }

    /**
     * 在指定 SessionTemplate 下创建 SessionExerciseTemplate。
     */
    @Override
    @Transactional
    public SessionExerciseTemplateResponse createSessionExerciseTemplate(
            Long userId,
            Long sessionTemplateId,
            CreateSessionExerciseTemplateRequest request
    ) {
        requireOwnedSessionTemplate(userId, sessionTemplateId);

        if (sessionExerciseTemplateRepository.existsBySessionTemplateIdAndOrderNo(
                sessionTemplateId,
                request.orderNo()
        )) {
            throw new BusinessException("SessionExerciseTemplate orderNo already exists in this session template");
        }

        // 模板定义只能引用当前用户真正可见的 Exercise，避免保存不可访问的动作引用。
        requireVisibleExercise(userId, request.exerciseId());

        LocalDateTime now = LocalDateTime.now();
        SessionExerciseTemplate templateExercise = new SessionExerciseTemplate(
                null,
                sessionTemplateId,
                request.exerciseId(),
                request.orderNo(),
                request.targetSets(),
                request.targetReps(),
                request.targetWeight(),
                normalize(request.targetWeightUnit()),
                request.restSeconds(),
                normalize(request.intensityMode()),
                request.progressionRuleId(),
                normalize(request.prescriptionJson()),
                now,
                now
        );
        return toSessionExerciseTemplateResponse(sessionExerciseTemplateRepository.save(templateExercise));
    }

    /**
     * 列出指定 SessionTemplate 下的 SessionExerciseTemplate。
     */
    @Override
    @Transactional(readOnly = true)
    public List<SessionExerciseTemplateResponse> listSessionExerciseTemplates(Long userId, Long sessionTemplateId) {
        requireOwnedSessionTemplate(userId, sessionTemplateId);
        return sessionExerciseTemplateRepository.findBySessionTemplateId(sessionTemplateId).stream()
                .map(ProgramApplicationServiceImpl::toSessionExerciseTemplateResponse)
                .toList();
    }

    /**
     * 更新一个属于当前用户 Program 层级的 SessionExerciseTemplate。
     */
    @Override
    @Transactional
    public SessionExerciseTemplateResponse updateSessionExerciseTemplate(
            Long userId,
            Long sessionExerciseTemplateId,
            UpdateSessionExerciseTemplateRequest request
    ) {
        SessionExerciseTemplate existing = requireOwnedSessionExerciseTemplate(userId, sessionExerciseTemplateId);

        if (sessionExerciseTemplateRepository.existsBySessionTemplateIdAndOrderNoAndIdNot(
                existing.sessionTemplateId(),
                request.orderNo(),
                existing.id()
        )) {
            throw new BusinessException("SessionExerciseTemplate orderNo already exists in this session template");
        }

        requireVisibleExercise(userId, request.exerciseId());

        SessionExerciseTemplate updated = new SessionExerciseTemplate(
                existing.id(),
                existing.sessionTemplateId(),
                request.exerciseId(),
                request.orderNo(),
                request.targetSets(),
                request.targetReps(),
                request.targetWeight(),
                normalize(request.targetWeightUnit()),
                request.restSeconds(),
                normalize(request.intensityMode()),
                request.progressionRuleId(),
                normalize(request.prescriptionJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toSessionExerciseTemplateResponse(sessionExerciseTemplateRepository.update(updated));
    }

    private Program requireOwnedProgram(Long userId, Long programId) {
        return programRepository.findByIdAndUserId(programId, userId)
                .orElseThrow(() -> new NotFoundException("Program not found"));
    }

    private ProgramBlock requireOwnedBlock(Long userId, Long blockId) {
        ProgramBlock block = programBlockRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException("ProgramBlock not found"));
        requireOwnedProgram(userId, block.programId());
        return block;
    }

    private SessionTemplate requireOwnedSessionTemplate(Long userId, Long templateId) {
        SessionTemplate template = sessionTemplateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("SessionTemplate not found"));
        requireOwnedBlock(userId, template.blockId());
        return template;
    }

    private SessionExerciseTemplate requireOwnedSessionExerciseTemplate(Long userId, Long templateExerciseId) {
        SessionExerciseTemplate templateExercise = sessionExerciseTemplateRepository.findById(templateExerciseId)
                .orElseThrow(() -> new NotFoundException("SessionExerciseTemplate not found"));
        requireOwnedSessionTemplate(userId, templateExercise.sessionTemplateId());
        return templateExercise;
    }

    private void requireVisibleExercise(Long userId, Long exerciseId) {
        if (exerciseRepository.findVisibleById(userId, exerciseId).isEmpty()) {
            throw new NotFoundException("Exercise not found");
        }
    }

    private static Boolean defaultFalse(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static ProgramResponse toProgramResponse(Program program) {
        return new ProgramResponse(
                program.id(),
                program.userId(),
                program.name(),
                program.goalType(),
                program.status(),
                program.description(),
                program.startDate(),
                program.endDate(),
                program.createdAt(),
                program.updatedAt()
        );
    }

    private static ProgramBlockResponse toProgramBlockResponse(ProgramBlock block) {
        return new ProgramBlockResponse(
                block.id(),
                block.programId(),
                block.name(),
                block.blockType(),
                block.sequenceNo(),
                block.durationMode(),
                block.durationValue(),
                block.deloadEnabled(),
                block.metadataJson(),
                block.createdAt(),
                block.updatedAt()
        );
    }

    private static SessionTemplateResponse toSessionTemplateResponse(SessionTemplate template) {
        return new SessionTemplateResponse(
                template.id(),
                template.blockId(),
                template.name(),
                template.sequenceNo(),
                template.triggerMode(),
                template.notes(),
                template.metadataJson(),
                template.createdAt(),
                template.updatedAt()
        );
    }

    private static SessionExerciseTemplateResponse toSessionExerciseTemplateResponse(
            SessionExerciseTemplate templateExercise
    ) {
        return new SessionExerciseTemplateResponse(
                templateExercise.id(),
                templateExercise.sessionTemplateId(),
                templateExercise.exerciseId(),
                templateExercise.orderNo(),
                templateExercise.targetSets(),
                templateExercise.targetReps(),
                templateExercise.targetWeight(),
                templateExercise.targetWeightUnit(),
                templateExercise.restSeconds(),
                templateExercise.intensityMode(),
                templateExercise.progressionRuleId(),
                templateExercise.prescriptionJson(),
                templateExercise.createdAt(),
                templateExercise.updatedAt()
        );
    }
}
