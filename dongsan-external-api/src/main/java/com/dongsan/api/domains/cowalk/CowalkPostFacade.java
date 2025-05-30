package com.dongsan.api.domains.cowalk;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkPostRequest;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.service.CowalkParticipantRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkPostRdbService;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;

@Service
@Transactional
public class CowalkPostFacade {
	private final CowalkParticipantRdbService cowalkParticipantRdbService;
	private final CowalkPostRdbService cowalkPostRdbService;
	private final CrewMemberRdbService crewMemberRdbService;

	public CowalkPostFacade(
		CowalkParticipantRdbService cowalkParticipantRdbService,
		CowalkPostRdbService cowalkPostRdbService,
		CrewMemberRdbService crewMemberRdbService
	) {
		this.cowalkParticipantRdbService = cowalkParticipantRdbService;
		this.cowalkPostRdbService = cowalkPostRdbService;
		this.crewMemberRdbService = crewMemberRdbService;
	}

	public Long saveCowalkPost(CreateCowalkPostRequest createCowalkPostRequest, Long crewId, Long memberId) {
		crewMemberRdbService.validateIsCrewMember(crewId, memberId);
		CreateCowalkPostCommand command = createCowalkPostRequest.toCreateCowalkPostCommand(crewId, memberId);
		Long cowalkPostId = cowalkPostRdbService.save(command);
		cowalkParticipantRdbService.save(memberId, cowalkPostId);
		return cowalkPostId;
	}
}
