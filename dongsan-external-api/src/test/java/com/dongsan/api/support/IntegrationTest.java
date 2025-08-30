package com.dongsan.api.support;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(scripts = "classpath:/reset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class IntegrationTest {
    static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:7.0.11")
            .withExposedPorts(6379)
            .withReuse(true);

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String DATABASE_NAME = "testDB";
    static final org.testcontainers.containers.MySQLContainer<?> MYSQL_CONTAINER =
            new org.testcontainers.containers.MySQLContainer<>(
                    "mysql:8.0.35")
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD);

    static {
        MYSQL_CONTAINER.start();
        REDIS_CONTAINER.start();
    }

    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // Testcontainers에서 제공하는 JDBC URL을 p6spy로 변경
        registry.add("spring.datasource.url", () -> "jdbc:p6spy:mysql://" + MYSQL_CONTAINER.getHost() + ":"
                + MYSQL_CONTAINER.getMappedPort(3306) + "/" + DATABASE_NAME);
        registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.data.redis.host", () -> REDIS_CONTAINER.getHost());
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    @BeforeEach
    void setupRestTemplate() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        restTemplate.getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

}
