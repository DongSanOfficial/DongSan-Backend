package com.dongsan.socket;

import com.dongsan.domain.domains.crew.CrewWalkCacheRepository;
import com.dongsan.domain.domains.crew.WalkData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WalkController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CrewWalkCacheRepository crewWalkCacheRepository;

    public WalkController(SimpMessagingTemplate simpMessagingTemplate, CrewWalkCacheRepository crewWalkCacheRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.crewWalkCacheRepository = crewWalkCacheRepository;
    }

    private static String getCrewCountDestination(Long crewId) {
        return "/topic/walk/crew/" + crewId + "/count";
    }

    private static String getCrewDetailDestination(Long crewId) {
        return "/topic/walk/crew/" + crewId + "/detail";
    }

    @MessageMapping("/walk/ongoing")
    public void ongoingWalk(
            OngoingWalkRequest request
    ) {
        for (Long crewId : request.crewIds()) {
            crewWalkCacheRepository.saveCrewMemberWalk(crewId, request.memberId(), request.nickname(),
                    request.distanceMeter(), request.timeMin());
            List<WalkData> walkDataList = crewWalkCacheRepository.getAllCrewMemberWalk(crewId);
            simpMessagingTemplate.convertAndSend(getCrewCountDestination(crewId), walkDataList.size());
            simpMessagingTemplate.convertAndSend(getCrewDetailDestination(crewId), walkDataList);
        }
    }

    @MessageMapping("/walk/end")
    public void endWalk(
            EndWalkRequest request
    ) {
        for (Long crewId : request.crewIds()) {
            crewWalkCacheRepository.deleteCrewMemberWalk(crewId, request.memberId());
            List<WalkData> walkDataList = crewWalkCacheRepository.getAllCrewMemberWalk(crewId);
            simpMessagingTemplate.convertAndSend(getCrewCountDestination(crewId), walkDataList.size());
            simpMessagingTemplate.convertAndSend(getCrewDetailDestination(crewId), walkDataList);
        }
    }

    //@MessageMapping("/walk/cowalk/{cowalkId}/ongoing")


}
