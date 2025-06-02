package com.dongsan.domain.domains.crew.service;

import org.springframework.stereotype.Service;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;

@Service
public class CrewMemberRdbService {
	private final CrewMemberRepository crewMemberRepository;

	public CrewMemberRdbService(CrewMemberRepository crewMemberRepository) {
		this.crewMemberRepository = crewMemberRepository;
	}

	public void saveManager(Long crewId, Long memberId) {
		CrewMember crewMember = new CrewMember(crewId, memberId, CrewMemberRole.MANAGER);
		crewMemberRepository.save(crewMember);
	}

	public void leaveCrew(Long crewId, Long memberId) {
		boolean isCrewMember = isCrewMember(crewId, memberId);
		if (!isCrewMember) {
			throw new CoreException(CoreErrorCode.CREW_NOT_JOINED);
		}
		crewMemberRepository.deleteByCrewIdAndMemberId(crewId, memberId);
	}

	private boolean isCrewMember(Long crewId, Long memberId) {
		return crewMemberRepository.existsByCrewIdAndMemberId(crewId, memberId);
	}

	public void validateIsCrewMember(Long crewId, Long memberId) {
		boolean isCrewMember = isCrewMember(crewId, memberId);
		if (!isCrewMember) {
			throw new CoreException(CoreErrorCode.CREW_NOT_JOINED);
		}
	}
}
