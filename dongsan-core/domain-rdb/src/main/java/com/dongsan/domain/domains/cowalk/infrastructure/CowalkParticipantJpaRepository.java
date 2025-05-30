package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;

@Repository
public interface CowalkParticipantJpaRepository extends JpaRepository<CowalkParticipant, Long> {
	Integer countByCowalkPostId(Long cowalkPostId);

	Boolean existsByMemberIdAndCowalkPostId(Long memberId, Long cowalkPostId);
}
