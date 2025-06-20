package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.domains.crew.domain.CrewMemberStatistic;
import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.domains.crew.domain.QCrewMember;
import com.dongsan.domain.support.paging.CursorResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
    private static final NumberExpression<Double> TOTAL_DISTANCE = Expressions.numberTemplate(Double.class,
            "COALESCE({0}, 0.0)", walkwayLog.distance.sum());
    private static final NumberExpression<Integer> TOTAL_TIME = Expressions.numberTemplate(Integer.class,
            "COALESCE({0}, 0)", walkwayLog.time.sum());
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
                .limit((long) size + 1)
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
    public CursorResponse<CrewMemberStatistic> getCrewRankingByDistance(Long crewId, Long lastMemberId, LocalDate startDay, LocalDate endDay, int size) {
        double lastDistance = getDistanceSum(crewId, lastMemberId, startDay, endDay);

        List<CrewMemberStatistic> result = queryFactory.select(Projections.constructor(
                        CrewMemberStatistic.class,
                        crewMember.memberId,
                        TOTAL_DISTANCE,
                        TOTAL_TIME
                ))
                .from(crewMember)
                .leftJoin(walkwayLog).on(walkwayLog.memberId.eq(crewMember.memberId)
                        .and(dateBetweenCondition(startDay, endDay)))
                .where(crewMember.crewId.eq(crewId))
                .having(distanceCondition(lastMemberId, lastDistance))
                .groupBy(crewMember.memberId)
                .orderBy(TOTAL_DISTANCE.desc(), crewMember.memberId.asc())
                .limit((long) size + 1)
                .fetch();

        return new CursorResponse<>(result, size);
    }

    @Override
    public CursorResponse<CrewMemberStatistic> getCrewRankingByTime(Long crewId, Long lastMemberId, LocalDate startDay, LocalDate endDay, int size) {
        int lastTime = getTimeSum(crewId, lastMemberId, startDay, endDay);

        List<CrewMemberStatistic> result = queryFactory.select(Projections.constructor(
                        CrewMemberStatistic.class,
                        crewMember.memberId,
                        TOTAL_DISTANCE,
                        TOTAL_TIME
                ))
                .from(crewMember)
                .leftJoin(walkwayLog).on(walkwayLog.memberId.eq(crewMember.memberId)
                        .and(dateBetweenCondition(startDay, endDay)))
                .where(crewMember.crewId.eq(crewId))
                .having(timeCondition(lastMemberId, lastTime))
                .groupBy(crewMember.memberId)
                .orderBy(TOTAL_TIME.desc(), crewMember.memberId.asc())
                .limit((long) size + 1)
                .fetch();

        return new CursorResponse<>(result, size);
    }

    private BooleanExpression distanceCondition(Long lastMemberId, double lastDistance) {
        if (lastMemberId == null) {
            return null;
        }
        return TOTAL_DISTANCE.lt(lastDistance)
                .or(TOTAL_DISTANCE.eq(lastDistance).and(crewMember.memberId.gt(lastMemberId)));
    }

    private BooleanExpression timeCondition(Long lastMemberId, int lastTime) {
        if (lastMemberId == null) {
            return null;
        }
        return TOTAL_TIME.lt(lastTime)
                .or(TOTAL_TIME.eq(lastTime).and(crewMember.memberId.gt(lastMemberId)));
    }

    private BooleanExpression dateBetweenCondition(LocalDate startDay, LocalDate endDay) {
        return walkwayLog.createdAt.between(startDay.atStartOfDay(), endDay.atTime(LocalTime.MAX));
    }

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    @Override
    public CrewWeeklyStatistic getCrewWeeklyStat(Long crewId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return queryFactory.select(Projections.constructor(
                        CrewWeeklyStatistic.class,
                        TOTAL_DISTANCE,
                        TOTAL_TIME
                ))
                .from(walkwayLog)
                .join(crewMember).on(crewMember.memberId.eq(walkwayLog.memberId))
                .where(crewMember.crewId.eq(crewId),
                        dateBetweenCondition(startOfWeek, endOfWeek)
                )
                .fetchOne();
    }

    private double getDistanceSum(Long crewId, Long memberId, LocalDate startDay, LocalDate endDay) {
        if (memberId == null) {
            return 0.0;
        }

        Double result = queryFactory
                .select(walkwayLog.distance.sum().coalesce(0.0))
                .from(walkwayLog)
                .join(crewMember).on(walkwayLog.memberId.eq(crewMember.memberId))
                .where(walkwayLog.memberId.eq(memberId),
                        crewMember.crewId.eq(crewId),
                        dateBetweenCondition(startDay, endDay))
                .fetchOne();

        return result == null ? 0.0 : result;
    }

    private int getTimeSum(Long crewId, Long memberId, LocalDate startDay, LocalDate endDay) {
        if (memberId == null) {
            return 0;
        }

        Integer result = queryFactory
                .select(walkwayLog.time.sum().coalesce(0))
                .from(walkwayLog)
                .join(crewMember).on(walkwayLog.memberId.eq(crewMember.memberId))
                .where(walkwayLog.memberId.eq(memberId),
                        crewMember.crewId.eq(crewId),
                        dateBetweenCondition(startDay, endDay))
                .fetchOne();

        return result == null ? 0 : result;
    }

    private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? walkwayLog.createdAt.lt(lastCreatedAt) : null;
    }


}
