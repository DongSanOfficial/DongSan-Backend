package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.CowalkParticipantRepository;

@Repository
public class CowalkParticipantCoreRepository implements CowalkParticipantRepository {
	private final CowalkParticipantJpaRepository cowalkParticipantJpaRepository;

	public CowalkParticipantCoreRepository(CowalkParticipantJpaRepository cowalkParticipantJpaRepository) {
		this.cowalkParticipantJpaRepository = cowalkParticipantJpaRepository;
	}

	@Override
	public CowalkParticipant save(CowalkParticipant cowalkParticipant) {
		return cowalkParticipantJpaRepository.save(cowalkParticipant);
	}
}
