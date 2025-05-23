package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
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
    public List<WalkwayLog> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
                                                       LocalDateTime lastCreatedAt) {
        return getCanReviewWalkwayHistories(
                walkwayId, memberId, size, lastCreatedAt);
    }

    @Override
    public List<WalkwayLog> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt);
    }


    @Override
    public void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed) {
        WalkwayLog walkwayLog = walkwayLogJpaRepository.getReferenceById(walkwayHistoryId);
        walkwayLog.updateIsReviewed(isReviewed);
        walkwayLogJpaRepository.save(walkwayLog);
    }

    @Override
    public boolean isReviewed(Long walkwayLogId) {
        return walkwayLogJpaRepository.existsByWalkwayLogId(walkwayLogId);
    }

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }


    // 산책로의 리뷰 가능한 회원의 기록 조회
    public List<WalkwayLog> getCanReviewWalkwayHistories(Long walkwayId, Long memberId, int size,
                                                         LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway)
                .fetchJoin()
                .where(
                        this.canReviewCondition(memberId),
                        walkwayHistory.walkway.id.eq(walkwayId),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    // 모든 산책로의 리뷰 가능한 회원의 기록 조회
    public List<WalkwayLog> getUserCanReviewWalkwayHistories(Long memberId, int size,
                                                             LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayHistory)
                .join(walkwayHistory.walkway)
                .fetchJoin()
                .where(
                        this.canReviewCondition(memberId),
                        walkwayHistory.walkway.exposeLevel.eq(ExposeLevel.PUBLIC),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(walkwayHistory.createdAt.desc())
                .fetch();
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? walkwayHistory.createdAt.lt(lastCreatedAt) : null;
    }

    private BooleanExpression canReviewCondition(Long memberId) {
        return walkwayHistory.member.id.eq(memberId)
                .and(walkwayHistory.isReviewed.eq(false))
                .and(walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0 / 3.0)));
    }
}
