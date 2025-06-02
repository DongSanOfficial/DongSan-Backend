package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CrewMemberCoreRepository implements CrewMemberRepository {
    private final CrewMemberJpaRepository crewMemberJpaRepository;

    public CrewMemberCoreRepository(CrewMemberJpaRepository crewMemberJpaRepository) {
        this.crewMemberJpaRepository = crewMemberJpaRepository;
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
    public void deleteByCrewIdAndMemberId(Long crewId, Long memberId) {
        crewMemberJpaRepository.deleteByCrewIdAndMemberId(crewId, memberId);
    }

    @Override
    public int countByCrewId(Long crewId) {
        return crewMemberJpaRepository.countByCrewId(crewId);
    }
}
