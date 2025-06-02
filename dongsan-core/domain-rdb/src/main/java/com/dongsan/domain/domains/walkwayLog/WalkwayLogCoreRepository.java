package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.domains.crew.domain.QCrewMember;
import com.dongsan.domain.support.util.CursorResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public class WalkwayLogCoreRepository implements WalkwayLogRepository {
    private static final QWalkwayLog walkwayLog = QWalkwayLog.walkwayLog;
    private static final QCrewMember crewMember = QCrewMember.crewMember;
    private final WalkwayLogJpaRepository walkwayLogJpaRepository;
    private final JPAQueryFactory queryFactory;

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
    public CursorResponse<WalkwayLog> getUserWalkwayLog(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<WalkwayLog> result = queryFactory.selectFrom(walkwayLog)
                .where(walkwayLog.memberId.eq(memberId),
                        createdAtLt(lastCreatedAt))
                .limit(size + 1)
                .orderBy(walkwayLog.createdAt.desc())
                .fetch();

        return new CursorResponse<>(result, size);
    }

    @Override
    public CursorResponse<WalkwayLog> getCrewWalkwayLog(Long crewId, LocalDateTime lastCreatedAt, int size) {
        List<WalkwayLog> result = queryFactory
                .selectFrom(walkwayLog)
                .join(crewMember).on(crewMember.memberId.eq(walkwayLog.memberId))
                .where(crewMember.crewId.eq(crewId),
                        createdAtLt(lastCreatedAt))
                .orderBy(walkwayLog.createdAt.desc())
                .limit((long) size + 1)
                .fetch();

        return new CursorResponse<>(result, size);
    }

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    @Override
    public CrewWeeklyStatistic getCrewWeeklyStat(Long crewId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return queryFactory.select(Projections.constructor(
                        CrewWeeklyStatistic.class,
                        walkwayLog.distance.sum().coalesce(0.0),
                        walkwayLog.time.sum().coalesce(0)
                ))
                .from(walkwayLog)
                .join(crewMember).on(crewMember.memberId.eq(walkwayLog.memberId))
                .where(crewMember.crewId.eq(crewId),
                        walkwayLog.createdAt.between(startOfWeek.atStartOfDay(),
                                endOfWeek.atTime(LocalTime.MAX))
                )
                .fetchOne();
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? walkwayLog.createdAt.lt(lastCreatedAt) : null;
    }
}
