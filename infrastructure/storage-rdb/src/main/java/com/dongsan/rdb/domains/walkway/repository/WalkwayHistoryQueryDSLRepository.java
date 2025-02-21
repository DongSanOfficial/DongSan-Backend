package com.dongsan.rdb.domains.walkway.repository;

import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.walkway.entity.QWalkwayHistoryEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayHistoryEntity;
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

    private QWalkwayHistoryEntity walkwayHistoryEntity = QWalkwayHistoryEntity.walkwayHistoryEntity;

    public List<WalkwayHistoryEntity> getCanReviewWalkwayHistories(Long walkwayId, Long memberId) {
        return queryFactory.selectFrom(walkwayHistoryEntity)
                .join(walkwayHistoryEntity.walkwayEntity).fetchJoin()
                .where(
                        walkwayHistoryEntity.memberEntity.id.eq(memberId),
                        walkwayHistoryEntity.walkwayEntity.id.eq(walkwayId),
                        walkwayHistoryEntity.distance.goe(walkwayHistoryEntity.walkwayEntity.distance.multiply(2.0/3.0)),
                        walkwayHistoryEntity.isReviewed.eq(false)
                )
                .orderBy(walkwayHistoryEntity.createdAt.desc())
                .fetch();
    }

    public List<WalkwayHistoryEntity> getUserCanReviewWalkwayHistories(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistoryEntity)
                .join(walkwayHistoryEntity.walkwayEntity).fetchJoin()
                .where(
                        walkwayHistoryEntity.memberEntity.id.eq(memberId),
                        walkwayHistoryEntity.distance.goe(walkwayHistoryEntity.walkwayEntity.distance.multiply(2.0/3.0)),
                        walkwayHistoryEntity.isReviewed.eq(false),
                        walkwayHistoryEntity.walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistoryEntity.createdAt.desc())
                .fetch();
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? walkwayHistoryEntity.createdAt.lt(lastCreatedAt) : null;
    }
}
