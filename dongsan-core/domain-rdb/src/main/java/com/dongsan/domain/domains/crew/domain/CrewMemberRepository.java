package com.dongsan.domain.domains.crew.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository {
    void save(CrewMember crewMember);

    boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

    void deleteByCrewIdAndMemberId(Long crewId, Long memberId);

    int countByCrewId(Long crewId);

    Map<Long, CrewMember> findMapByCrewIdAndMemberId(List<Long> crewIds, Long memberId);

    Map<Long, Integer> countByCrewIds(List<Long> crewIds);
}
