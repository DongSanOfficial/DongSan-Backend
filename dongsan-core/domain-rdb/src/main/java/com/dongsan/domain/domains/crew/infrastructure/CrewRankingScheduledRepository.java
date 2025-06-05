package com.dongsan.domain.domains.crew.infrastructure;

import static com.dongsan.domain.domains.crew.domain.QCrew.*;
import static com.dongsan.domain.domains.crew.domain.QCrewMember.*;
import static com.dongsan.domain.domains.member.QMember.*;
import static com.dongsan.domain.domains.walkwayLog.QWalkwayLog.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.crew.domain.CrewRanking;
import com.dongsan.domain.domains.crew.domain.QCrewRanking;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@Transactional
public class CrewRankingScheduledRepository {
    private final JPAQueryFactory queryFactory;
    private final CrewRankingJpaRepository crewRankingJpaRepository;
    private final QCrewRanking crewRanking = QCrewRanking.crewRanking;

    public CrewRankingScheduledRepository(JPAQueryFactory queryFactory,
            CrewRankingJpaRepository crewRankingJpaRepository) {
        this.queryFactory = queryFactory;
        this.crewRankingJpaRepository = crewRankingJpaRepository;
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void resetCrewRanking() {
        queryFactory.delete(crewRanking).execute();
    }

    @Scheduled(cron = "0 0 1-23 * * *")
    public void updateCrewRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);

        // 지난 1시간 동안 각 Crew별 log 수를 집계
        List<Tuple> results = queryFactory
                .select(crew.id, walkwayLog.count())
                .from(walkwayLog)
                .join(member).on(walkwayLog.memberId.eq(member.id))
                .join(crewMember).on(crewMember.memberId.eq(member.id))
                .join(crew).on(crew.id.eq(crewMember.crewId))
                .where(walkwayLog.createdAt.between(oneHourAgo, now))
                .groupBy(crew.id)
                .fetch();

        // 집계된 결과를 CrewRanking에 반영
        for (Tuple result : results) {
            Long crewId = result.get(crew.id);
            Long count = result.get(walkwayLog.count());

            CrewRanking ranking = queryFactory
                    .selectFrom(crewRanking)
                    .where(crewRanking.crewId.eq(crewId))
                    .fetchOne();

            if (ranking == null) {
                ranking = new CrewRanking(crewId);
            }

            ranking.addLogCount(count);
            crewRankingJpaRepository.save(ranking);
        }
    }
}
