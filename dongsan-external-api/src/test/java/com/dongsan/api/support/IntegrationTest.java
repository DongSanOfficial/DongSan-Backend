package com.dongsan.api.support;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(scripts = "classpath:/reset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class IntegrationTest {
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String DATABASE_NAME = "testDB";
    @Container
    public static org.testcontainers.containers.MySQLContainer<?> mySQLContainer =
            new org.testcontainers.containers.MySQLContainer<>(
                    "mysql:8.0.35")
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD);
    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.11")
            .withExposedPorts(6379);
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // Testcontainers에서 제공하는 JDBC URL을 p6spy로 변경
        registry.add("spring.datasource.url", () -> "jdbc:p6spy:mysql://" + mySQLContainer.getHost() + ":"
                + mySQLContainer.getMappedPort(3306) + "/" + DATABASE_NAME);
        registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.data.redis.host", () -> redisContainer.getHost());
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @BeforeEach
    void setupRestTemplate() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        restTemplate.getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

}
