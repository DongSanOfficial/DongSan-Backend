package com.dongsan.common.config;

import com.dongsan.domains.bookmark.repository.BookmarkQueryDSLRepository;
import com.dongsan.rdb.domains.bookmark.MarkedWalkwayQueryDSLRepository;
import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.dongsan.domains.walkway.repository.WalkwayHistoryQueryDSLRepository;
import com.dongsan.rdb.domains.walkway.repository.HashtagDSLRepository;
import com.dongsan.rdb.domains.walkway.LikedWalkwayQueryDSLRepository;
import com.dongsan.rdb.domains.walkway.WalkwayQueryDSLRepository;
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
    public ReviewQueryDSLRepository reviewQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new ReviewQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public BookmarkQueryDSLRepository bookmarkQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new BookmarkQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public HashtagDSLRepository hashtagDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new HashtagDSLRepository(jpaQueryFactory);
    }

    @Bean
    public WalkwayQueryDSLRepository walkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new WalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new LikedWalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new MarkedWalkwayQueryDSLRepository(jpaQueryFactory);
    }

    @Bean
    public WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new WalkwayHistoryQueryDSLRepository(jpaQueryFactory);
    }
}
