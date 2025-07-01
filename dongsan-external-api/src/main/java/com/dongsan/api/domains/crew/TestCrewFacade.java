package com.dongsan.api.domains.crew;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.lock.RedisLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestCrewFacade {
    private static final Logger log = LoggerFactory.getLogger(TestCrewFacade.class);
    private final CrewRdbService crewRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final RedisLockService redisLockService;

    public TestCrewFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService, RedisLockService redisLockService) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.redisLockService = redisLockService;
    }

    //@Transactional
    public void joinLimitedCrew(Long crewId, Long memberId) {
        redisLockService.callWithLock(crewId, () -> {
            Crew crew = crewRdbService.getCrewWithLock(crewId);
            int memberCount = crewMemberRdbService.countCrewMember(crewId);
            // crewRdbService.comparePassword(crew, password);
            // crewMemberRdbService.validateNotAlreadyJoined(crewId, memberId);
            crewMemberRdbService.joinCrew(crewId, memberId);

            return null;
        });
    }

}
