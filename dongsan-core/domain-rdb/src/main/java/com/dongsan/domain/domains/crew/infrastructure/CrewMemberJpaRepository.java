package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberJpaRepository extends JpaRepository<CrewMember, Long> {
    boolean existsByCrewIdAndMemberId(Long crewId, Long memberId);

    boolean existsByCrewIdAndMemberIdAndRole(Long crewId, Long memberId, CrewMemberRole role);

    int countByCrewId(Long crewId);

    void deleteByCrewIdAndMemberId(Long crewId, Long memberId);
}
