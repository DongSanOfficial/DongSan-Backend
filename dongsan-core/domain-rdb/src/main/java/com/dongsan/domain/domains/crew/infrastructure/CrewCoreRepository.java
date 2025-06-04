package com.dongsan.domain.domains.crew.infrastructure;

import static com.dongsan.domain.domains.crew.domain.QCrewMember.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
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
    public Long save(Crew crew) {
        return crewJpaRepository.save(crew).getId();
    }

    @Override
    public boolean existsByName(String name) {
        return crewJpaRepository.existsByName(name.trim());
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
                .limit(size + 1)
                .fetch();

        return new CursorResponse<>(crewList, size);
    }

    private BooleanExpression crewIdLt(Long id) {
        return id == null ? null : crew.id.lt(id);
    }
}
