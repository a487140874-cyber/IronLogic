package com.ironlogic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IronLogic backend application entrypoint.
 *
 * <p>This class stays intentionally small: its only responsibility is to bootstrap the
 * Spring Boot modular monolith. The explicit {@link MapperScan} is important here because
 * the project uses MyBatis-Plus and mapper interfaces live inside module-specific
 * infrastructure packages rather than in the root package.
 */
@SpringBootApplication
@MapperScan(basePackages = {
        "com.ironlogic.modules.exercise.infrastructure.persistence.mapper",
        "com.ironlogic.modules.program.infrastructure.persistence.mapper",
        "com.ironlogic.modules.progression.infrastructure.persistence.mapper",
        "com.ironlogic.modules.workout.infrastructure.persistence.mapper"
})
public class IronLogicApplication {

    /**
     * Starts the backend application.
     *
     * @param args standard Spring Boot startup arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(IronLogicApplication.class, args);
    }
}
