package com.dongsan.api.domains.crew;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.lock.RedisLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestCrewFacade {
    private final CrewRdbService crewRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final RedisLockService redisLockService;

    public TestCrewFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService, RedisLockService redisLockService) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.redisLockService = redisLockService;
    }

    @Transactional
    public void joinLimitedCrew(Long crewId, Long memberId) {
        redisLockService.callWithLock(crewId, () -> {
            Crew crew = crewRdbService.getCrewWithLock(crewId);
            int memberCount = crewMemberRdbService.countCrewMember(crewId);
            crew.validateNotFull(memberCount);

            // crewRdbService.comparePassword(crew, password);
            // crewMemberRdbService.validateNotAlreadyJoined(crewId, memberId);
            crewMemberRdbService.joinCrew(crewId, memberId);

            return null;
        });
    }
    
}
