package com.dongsan.domain.domains.crew.infrastructure;

import static com.dongsan.domain.domains.crew.domain.QCrewMember.*;
import static com.dongsan.domain.domains.crew.domain.QMetaCrewRanking.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
import com.dongsan.domain.domains.crew.domain.MetaCrewRanking;
import com.dongsan.domain.domains.crew.domain.QCrew;
import com.dongsan.domain.support.paging.CursorResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CrewCoreRepository implements CrewRepository {
    private final CrewJpaRepository crewJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QCrew crew = QCrew.crew;

    public CrewCoreRepository(CrewJpaRepository crewJpaRepository, JPAQueryFactory queryFactory) {
        this.crewJpaRepository = crewJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Crew> findById(Long crewId) {
        return crewJpaRepository.findById(crewId);
    }

    @Override
    public Optional<Crew> findByIdWithLock(Long crewId) {
        return crewJpaRepository.findByIdWithLock(crewId);
    }

    @Override
    public Long save(Crew crew) {
        return crewJpaRepository.save(crew).getId();
    }

    @Override
    public boolean existsByName(String name) {
        return crewJpaRepository.existsByInfo_Name(name.trim());
    }

    @Override
    public CursorResponse<Crew> getMyCrews(Long memberId, Integer size, Long lastId) {
        List<Crew> crewList = queryFactory
                .selectFrom(crew)
                .join(crewMember).on(crew.id.eq(crewMember.crewId))
                .where(
                        crewMember.memberId.eq(memberId),
                        crewIdLt(lastId)
                )
                .orderBy(crew.id.desc())
                .limit(size + 1L)
                .fetch();

        return new CursorResponse<>(crewList, size);
    }

    @Override
    public CursorResponse<Crew> searchCrews(String name, Integer size, Long lastId) {
        List<Crew> crewList = queryFactory
                .selectFrom(crew)
                .where(
                        nameLike(name),
                        crewIdLt(lastId)
                )
                .orderBy(crew.id.desc())
                .limit(size + 1L)
                .fetch();

        return new CursorResponse<>(crewList, size);
    }

    @Override
    public CursorResponse<Crew> findCrewsByLogThisWeek(int size, Long lastId, Long memberId) {
        MetaCrewRanking cursorRanking = this.findCursorRanking(lastId);

        // 랭킹에 있는 count 기준으로 불러오기
        List<Crew> crewList = queryFactory.selectFrom(crew)
                .join(metaCrewRanking).on(crew.id.eq(metaCrewRanking.crewId))
                .leftJoin(crewMember)
                .on(crewMember.crewId.eq(crew.id).and(crewMember.memberId.eq(memberId)))
                .where(
                        cursorRankingCondition(cursorRanking),
                        crewMember.id.isNull()
                )
                .limit(size + 1L)
                .orderBy(metaCrewRanking.logCount.desc(), metaCrewRanking.updatedAt.asc(), metaCrewRanking.crewId.asc())
                .fetch();

        return new CursorResponse<>(crewList, size);
    }

    @Override
    public List<Long> getAllMyCrewIds(Long memberId) {
        return queryFactory
                .select(crew.id)
                .from(crew)
                .join(crewMember)
                .on(crew.id.eq(crewMember.crewId).and(crewMember.memberId.eq(memberId)))
                .orderBy(crew.id.desc())
                .fetch();
    }

    private MetaCrewRanking findCursorRanking(Long lastId) {
        if (lastId == null)
            return null;

        return queryFactory.selectFrom(metaCrewRanking)
                .where(metaCrewRanking.crewId.eq(lastId))
                .fetchOne();
    }

    private BooleanExpression cursorRankingCondition(MetaCrewRanking cursorRanking) {
        if (cursorRanking == null)
            return null;

        return metaCrewRanking.logCount.lt(cursorRanking.getLogCount())
                .or(metaCrewRanking.logCount.eq(cursorRanking.getLogCount())
                        .and(metaCrewRanking.updatedAt.gt(cursorRanking.getUpdatedAt())))
                .or(metaCrewRanking.logCount.eq(cursorRanking.getLogCount())
                        .and(metaCrewRanking.updatedAt.eq(cursorRanking.getUpdatedAt()))
                        .and(metaCrewRanking.crewId.gt(cursorRanking.getCrewId())));
    }

    private BooleanExpression crewIdLt(Long id) {
        return id == null ? null : crew.id.lt(id);
    }

    private BooleanExpression nameLike(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return crew.info.name.like("%" + name + "%");
    }
}
