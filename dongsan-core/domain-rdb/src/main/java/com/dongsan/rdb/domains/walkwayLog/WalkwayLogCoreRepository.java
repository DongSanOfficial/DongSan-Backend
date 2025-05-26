package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WalkwayLogCoreRepository implements WalkwayLogRepository {
    private final WalkwayLogJpaRepository walkwayLogJpaRepository;
    private final JPAQueryFactory queryFactory;
    private QWalkwayLog walkwayLog = QWalkwayLog.walkwayLog;

    public WalkwayLogCoreRepository(WalkwayLogJpaRepository walkwayLogJpaRepository, JPAQueryFactory queryFactory) {
        this.walkwayLogJpaRepository = walkwayLogJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<WalkwayLog> findById(Long walkwayHistoryId) {
        return walkwayLogJpaRepository.findById(walkwayHistoryId);
    }

    @Override
    public Long save(WalkwayLog walkwayLog) {
        return walkwayLogJpaRepository.save(walkwayLog).getId();
    }

    @Override
    public CursorPage<WalkwayLog> getUserWalkwayLog(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<WalkwayLog> result = queryFactory.selectFrom(walkwayLog)
                .where(walkwayLog.memberId.eq(memberId),
                        createdAtLt(lastCreatedAt))
                .limit(size + 1)
                .orderBy(walkwayLog.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    @Override
    public boolean isReviewed(Long walkwayLogId) {
        return walkwayLogJpaRepository.existsByWalkwayLogId(walkwayLogId);
    }

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? walkwayLog.createdAt.lt(lastCreatedAt) : null;
    }

//    private BooleanExpression canReviewCondition(Long memberId, Double walkwayDistance) {
//        return walkwayLog.memberId.eq(memberId)
//                .and(walkwayLog.isReviewed.eq(false))
//                .and(walkwayLog.distance.goe(walkwayDistance * (2.0 / 3.0)));
//    }

//    @Override
//    public List<WalkwayLog> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
//                                                       LocalDateTime lastCreatedAt) {
//        return queryFactory.selectFrom(walkwayHistory)
//                .join(walkwayHistory.walkway)
//                .fetchJoin()
//                .where(
//                        this.canReviewCondition(memberId),
//                        walkwayHistory.walkway.id.eq(walkwayId),
//                        createdAtLt(lastCreatedAt)
//                )
//                .limit(size)
//                .orderBy(walkwayHistory.createdAt.desc())
//                .fetch();
//    }
//
//    @Override
//    public List<WalkwayLog> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt) {
//        return queryFactory.selectFrom(walkwayHistory)
//                .join(walkwayHistory.walkway)
//                .fetchJoin()
//                .where(
//                        this.canReviewCondition(memberId),
//                        walkwayHistory.walkway.exposeLevel.eq(ExposeLevel.PUBLIC),
//                        createdAtLt(lastCreatedAt)
//                )
//                .limit(size)
//                .orderBy(walkwayHistory.createdAt.desc())
//                .fetch();
//    }

//    @Override
//    public void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed) {
//        WalkwayLog walkwayLog = walkwayLogJpaRepository.getReferenceById(walkwayHistoryId);
//        walkwayLog.updateIsReviewed(isReviewed);
//        walkwayLogJpaRepository.save(walkwayLog);
//    }
}
