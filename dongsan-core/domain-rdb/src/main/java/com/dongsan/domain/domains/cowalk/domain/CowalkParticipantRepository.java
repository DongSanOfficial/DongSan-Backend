package com.dongsan.domain.domains.cowalk.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkParticipantRepository {
	CowalkParticipant save(CowalkParticipant cowalkParticipant);

	Integer countByCowalkPostId(Long cowalkPostId);

	Boolean existsByMemberIdAndCowalkPostId(Long memberId, Long cowalkPostId);
}
