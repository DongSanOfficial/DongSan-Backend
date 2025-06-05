package com.dongsan.domain.domains.crew.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository {
    void save(CrewMember crewMember);

    boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

    boolean existsByCrewIdAndMemberIdAndRole(Long crewId, Long memberId, CrewMemberRole crewMemberRole);

    void deleteByCrewIdAndMemberId(Long crewId, Long memberId);

    int countByCrewId(Long crewId);
}
