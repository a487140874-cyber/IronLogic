package com.ironlogic;

import com.ironlogic.modules.exercise.infrastructure.persistence.mapper.ExerciseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class IronLogicApplicationTests {

    @MockBean
    private ExerciseMapper exerciseMapper;

    @Test
    void contextLoads() {
    }
}
