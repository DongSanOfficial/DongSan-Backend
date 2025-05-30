package com.dongsan.domain.domains.cowalk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.CowalkParticipantRepository;

@Service
@Transactional
public class CowalkParticipantRdbService {
	private final CowalkParticipantRepository cowalkParticipantRepository;

	public CowalkParticipantRdbService(CowalkParticipantRepository cowalkParticipantRepository) {
		this.cowalkParticipantRepository = cowalkParticipantRepository;
	}

	public Long save(Long memberId, Long cowalkPostId) {
		CowalkParticipant cowalkParticipant = new CowalkParticipant(cowalkPostId, memberId);
		cowalkParticipantRepository.save(cowalkParticipant);
		return cowalkParticipant.getId();
	}
}
