package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.domains.walkway.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class WalkwayHistoryQueryDSLRepository {
    private final JPAQueryFactory queryFactory;

    public WalkwayHistoryQueryDSLRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private QWalkwayHistoryEntity walkwayHistory = QWalkwayHistoryEntity.walkwayHistoryEntity;

    public List<WalkwayHistoryEntity> getCanReviewWalkwayHistories(Long walkwayId, Long memberId, int size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway).fetchJoin()
                .where(
                        walkwayHistory.member.id.eq(memberId),
                        walkwayHistory.walkway.id.eq(walkwayId),
                        walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0/3.0)),
                        walkwayHistory.isReviewed.eq(false),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    public List<WalkwayHistoryEntity> getUserCanReviewWalkwayHistories(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway).fetchJoin()
                .where(
                        walkwayHistory.member.id.eq(memberId),
                        walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0/3.0)),
                        walkwayHistory.isReviewed.eq(false),
                        walkwayHistory.walkway.exposeLevel.eq(ExposeLevel.PUBLIC),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? walkwayHistory.createdAt.lt(lastCreatedAt) : null;
    }
}
