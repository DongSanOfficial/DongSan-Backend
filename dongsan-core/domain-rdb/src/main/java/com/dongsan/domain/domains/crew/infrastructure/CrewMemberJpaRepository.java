package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberJpaRepository extends JpaRepository<CrewMember, Long> {
    boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

    int countByCrewId(Long crewId);

    void deleteByCrewIdAndMemberId(Long crewId, Long memberId);
}
