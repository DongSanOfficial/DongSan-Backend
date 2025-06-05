package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class CrewMemberRdbService {
    private final CrewMemberRepository crewMemberRepository;

    public CrewMemberRdbService(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    public void saveCrewManager(Long crewId, Long memberId) {
        CrewMember crewMember = new CrewMember(crewId, memberId, CrewMemberRole.MANAGER);
        crewMemberRepository.save(crewMember);
    }

    public void leaveCrew(Long crewId, Long memberId) {
        validateIsCrewMember(crewId, memberId);
        crewMemberRepository.deleteByCrewIdAndMemberId(crewId, memberId);
    }

    public boolean isCrewMember(Long crewId, Long memberId) {
        return crewMemberRepository.existsByCrewIdAndMemberId(crewId, memberId);
    }

    public boolean isCrewManager(Long crewId, Long memberId) {
        return crewMemberRepository.existsByCrewIdAndMemberIdAndRole(crewId, memberId, CrewMemberRole.MANAGER);
    }

    public void validateIsCrewMember(Long crewId, Long memberId) {
        boolean isCrewMember = isCrewMember(crewId, memberId);
        if (!isCrewMember) {
            throw new CoreException(CoreErrorCode.CREW_NOT_JOINED);
        }
    }

    public void validateIsCrewManager(Long crewId, Long memberId) {
        boolean isCrewManager = isCrewManager(crewId, memberId);
        if (!isCrewManager) {
            throw new CoreException(CoreErrorCode.CREW_NOT_MANAGER);
        }
    }

    public void joinCrew(Long crewId, Long memberId) {
        CrewMember crewMember = new CrewMember(crewId, memberId, CrewMemberRole.PARTICIPANT);
        crewMemberRepository.save(crewMember);
    }

    public int countCrewMember(Long crewId) {
        return crewMemberRepository.countByCrewId(crewId);
    }

    public void validateNotAlreadyJoined(Long crewId, Long memberId) {
        boolean hasJoined = isCrewMember(crewId, memberId);
        if (hasJoined) {
            throw new CoreException(CoreErrorCode.CREW_ALREADY_JOINED);
        }
    }
}
