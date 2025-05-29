package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import org.springframework.stereotype.Service;

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
}
