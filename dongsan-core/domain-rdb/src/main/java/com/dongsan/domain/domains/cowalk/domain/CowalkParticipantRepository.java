package com.dongsan.domain.domains.cowalk.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkParticipantRepository {
	public CowalkParticipant save(CowalkParticipant cowalkParticipant);
}
