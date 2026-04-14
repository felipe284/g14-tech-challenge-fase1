package com.fiap_g14.foodlink.api.integration;

import com.fiap_g14.foodlink.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15.3")
                    .withDatabaseName("foodlink-test")
                    .withUsername("test")
                    .withPassword("test");
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", UserControllerIT.postgres::getJdbcUrl);
        registry.add("spring.datasource.username", UserControllerIT.postgres::getUsername);
        registry.add("spring.datasource.password", UserControllerIT.postgres::getPassword);
    }


    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        userRepository.saveAll(List.of(
                new User(null, "João"),
                new User(null, "Maria")
        ));
    }
