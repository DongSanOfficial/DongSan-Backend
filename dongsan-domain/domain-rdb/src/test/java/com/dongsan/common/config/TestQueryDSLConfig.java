package com.dongsan.common.config;

import com.dongsan.rdb.domains.review.ReviewCoreRepository;
import com.dongsan.rdb.domains.review.ReviewJpaRepository;
import com.dongsan.rdb.domains.walkway.*;
import com.dongsan.rds.domains.member.MemberJpaRepository;
import com.querydsl.jpa.JPQLTemplates;
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
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    @Bean
    public ReviewCoreRepository reviewCoreRepository(ReviewJpaRepository reviewJpaRepository,
                                                     MemberJpaRepository memberJpaRepository,
                                                     WalkwayJpaRepository walkwayJpaRepository,
                                                     WalkwayHistoryJpaRepository walkwayHistoryJpaRepository,
                                                     JPAQueryFactory jpaQueryFactory) {
        return new ReviewCoreRepository(reviewJpaRepository, memberJpaRepository, walkwayJpaRepository,
                walkwayHistoryJpaRepository, jpaQueryFactory);
    }

    @Bean
    public WalkwayQueryDSLRepository walkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        return new WalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        return new LikedWalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        return new WalkwayHistoryQueryDSLRepository(jpaQueryFactory);
    }
}
