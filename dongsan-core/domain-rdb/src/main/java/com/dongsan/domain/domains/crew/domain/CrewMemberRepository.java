package com.dongsan.domain.domains.crew.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository {
    void save(CrewMember crewMember);

    boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

    void deleteByCrewIdAndMemberId(Long crewId, Long memberId);
}
