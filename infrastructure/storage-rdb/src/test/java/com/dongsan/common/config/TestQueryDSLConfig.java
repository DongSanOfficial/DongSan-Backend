package com.dongsan.common.config;

import com.dongsan.rdb.domains.walkway.repository.LikedWalkwayQueryDSLRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayQueryDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestQueryDSLConfig {
    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public WalkwayQueryDSLRepository walkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new WalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new LikedWalkwayQueryDSLRepository(jpaQueryFactory);
    }

}
