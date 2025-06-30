package com.dongsan.domain.domains.crew.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import com.dongsan.domain.domains.crew.domain.QCrewMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CrewMemberCoreRepository implements CrewMemberRepository {
    private final CrewMemberJpaRepository crewMemberJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QCrewMember crewMember = QCrewMember.crewMember;

    public CrewMemberCoreRepository(CrewMemberJpaRepository crewMemberJpaRepository, JPAQueryFactory queryFactory) {
        this.crewMemberJpaRepository = crewMemberJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(CrewMember crewMember) {
        crewMemberJpaRepository.save(crewMember);
    }

    @Override
    public boolean existsByCrewIdAndMemberId(Long crewId, Long memberId) {
        return crewMemberJpaRepository.existsByCrewIdAndMemberId(crewId, memberId);
    }

    @Override
    public boolean existsByCrewIdAndMemberIdAndRole(Long crewId, Long memberId, CrewMemberRole role) {
        return crewMemberJpaRepository.existsByCrewIdAndMemberIdAndRole(crewId, memberId, role);
    }

    @Override
    public void deleteByCrewIdAndMemberId(Long crewId, Long memberId) {
        crewMemberJpaRepository.deleteByCrewIdAndMemberId(crewId, memberId);
    }

    @Override
    public int countByCrewId(Long crewId) {
        return crewMemberJpaRepository.countByCrewId(crewId);
    }

    @Override
    public Map<Long, CrewMember> findMapByCrewIdAndMemberId(List<Long> crewIds, Long memberId) {
        List<CrewMember> crewMemberList = queryFactory.selectFrom(crewMember)
                .where(
                        crewMember.memberId.eq(memberId),
                        crewMember.crewId.in(crewIds)
                )
                .fetch();

        return crewMemberList.stream()
                .collect(Collectors.toMap(
                        CrewMember::getCrewId,
                        Function.identity()
                ));
    }

    @Override
    public Map<Long, Integer> countByCrewIds(List<Long> crewIds) {
        List<Tuple> countTuple = queryFactory
                .select(crewMember.crewId, crewMember.count())
                .from(crewMember)
                .where(crewMember.crewId.in(crewIds))
                .groupBy(crewMember.crewId)
                .fetch();

        return countTuple.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(crewMember.crewId),
                        tuple -> tuple.get(crewMember.count()).intValue()
                ));
    }

    @Override
    public CrewMember findByCrewIdAndRole(Long crewId, CrewMemberRole crewMemberRole) {
        return crewMemberJpaRepository.findByCrewIdAndRole(crewId, crewMemberRole);
    }
}
