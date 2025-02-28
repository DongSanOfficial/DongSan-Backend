package com.dongsan.common.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.dongsan.common.config.TestQueryDSLConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Import(TestQueryDSLConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@EnableJpaAuditing
@Testcontainers
@DirtiesContext
public abstract class RepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String DATABASE_NAME = "testDB";

    @Container
    public static org.testcontainers.containers.MySQLContainer<?> mySQLContainer = new org.testcontainers.containers.MySQLContainer<>("mysql:8.0.35")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            ;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    public void setUpTables(){
        resetIds();
    }

    private void resetIds(){
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        for (var entity : entities) {
            // 엔티티 클래스 이름을 소문자로 변환하고 언더스코어로 연결
            String tableName = getTableName(entity);

            // 1. DELETE 명령으로 모든 데이터 삭제
            jdbcTemplate.execute("DELETE FROM " + tableName);

            // 2. 기본 키를 1로 초기화
            jdbcTemplate.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
        }
    }

    private String getTableName(EntityType<?> entity){
        // 엔티티가 @Table 애노테이션을 가지고 있는지 확인
        Table tableAnnotation = entity.getJavaType().getAnnotation(Table.class);
        if (tableAnnotation != null) {
            // @Table 애노테이션이 있으면 그 이름을 반환
            return tableAnnotation.name();
        }
        // @Table 애노테이션이 없으면 클래스 이름을 변환하여 반환
        return convertToTableName(entity.getJavaType().getSimpleName());
    }

    private String convertToTableName(String className) {
        // 클래스 이름을 소문자로 변환하고 언더스코어로 변환 (CamelCase 처리)
        StringBuilder tableName = new StringBuilder();
        for (char c : className.toCharArray()) {
            if (Character.isUpperCase(c) && tableName.length() > 0) {
                tableName.append('_');
            }
            tableName.append(Character.toLowerCase(c));
        }
        return tableName.toString();
    }
}
