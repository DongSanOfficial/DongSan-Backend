package com.dongsan.api.domains.crew;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.lock.RedisLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrewJoinFacade {
    private final CrewMemberRdbService crewMemberRdbService;
    private final CrewRdbService crewRdbService;
    private final RedisLockService redisLockService;

    public CrewJoinFacade(CrewMemberRdbService crewMemberRdbService, CrewRdbService crewRdbService, RedisLockService redisLockService) {
        this.crewMemberRdbService = crewMemberRdbService;
        this.crewRdbService = crewRdbService;
        this.redisLockService = redisLockService;
    }

    @Transactional
    public void joinUnLimitedCrew(Crew crew, Long memberId, String password) {
        crewMemberRdbService.validateNotAlreadyJoined(crew.getId(), memberId);
        crewRdbService.comparePassword(crew, password);
        crewMemberRdbService.joinCrew(crew.getId(), memberId);
    }

    public void joinLimitedCrew(Crew crew, Long memberId, String password) {
        redisLockService.callWithLock(crew.getId(), () -> {
            crewRdbService.comparePassword(crew, password);

            int memberCount = crewMemberRdbService.countCrewMember(crew.getId());
            crew.validateNotFull(memberCount);
            crewMemberRdbService.validateNotAlreadyJoined(crew.getId(), memberId);
            crewMemberRdbService.joinCrew(crew.getId(), memberId);

            return null;
        });
    }
}
