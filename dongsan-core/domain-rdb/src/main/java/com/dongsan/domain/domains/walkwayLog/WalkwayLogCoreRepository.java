package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.support.util.CursorPage;
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
    private final QWalkwayLog walkwayLog = QWalkwayLog.walkwayLog;

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
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? walkwayLog.createdAt.lt(lastCreatedAt) : null;
    }
}
