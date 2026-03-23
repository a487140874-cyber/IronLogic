package com.ironlogic.modules.program.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.program.dto.CreateProgramRequest;
import com.ironlogic.modules.program.dto.ProgramResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link ProgramApplicationServiceImpl} 的聚焦单元测试。
 *
 * <p>这个测试验证 MVP 阶段创建 Program 的主流程，不依赖数据库或 Web 上下文。
 */
@ExtendWith(MockitoExtension.class)
class ProgramApplicationServiceImplTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramBlockRepository programBlockRepository;

    @Mock
    private SessionTemplateRepository sessionTemplateRepository;

    @Mock
    private SessionExerciseTemplateRepository sessionExerciseTemplateRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ProgramApplicationServiceImpl programApplicationService;

    /** 验证创建 Program 时会正确绑定到当前用户。 */
    @Test
    void shouldCreateProgramForCurrentUser() {
        CreateProgramRequest request = new CreateProgramRequest(
                "Push Pull Legs",
                "hypertrophy",
                "draft",
                "Three day rotation",
                LocalDate.of(2026, 3, 20),
                LocalDate.of(2026, 6, 20)
        );

        when(programRepository.save(any(Program.class))).thenAnswer(invocation -> {
            Program program = invocation.getArgument(0);
            return new Program(
                    100L,
                    program.userId(),
                    program.name(),
                    program.goalType(),
                    program.status(),
                    program.description(),
                    program.startDate(),
                    program.endDate(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        });

        ProgramResponse response = programApplicationService.createProgram(1L, request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Push Pull Legs");

        ArgumentCaptor<Program> captor = ArgumentCaptor.forClass(Program.class);
        verify(programRepository).save(captor.capture());
        assertThat(captor.getValue().userId()).isEqualTo(1L);
        assertThat(captor.getValue().id()).isNull();
    }
}
