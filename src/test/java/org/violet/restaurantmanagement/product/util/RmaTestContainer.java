package org.violet.restaurantmanagement.product.util;

import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface RmaTestContainer {
    @Container
    PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("restaurant_user")
            .withPassword("restaurant_password")
            .withDatabaseName("restaurant_management");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        POSTGRESQL_CONTAINER.start();
        dynamicPropertyRegistry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
    }

    @AfterAll
    static void stopContainer() {
        POSTGRESQL_CONTAINER.stop();
    }
}
