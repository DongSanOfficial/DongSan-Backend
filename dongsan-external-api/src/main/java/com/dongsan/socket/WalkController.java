package com.dongsan.socket;

import com.dongsan.domain.cache.CowalkCacheRepository;
import com.dongsan.domain.cache.CrewWalkCacheRepository;
import com.dongsan.domain.cache.WalkData;
import com.dongsan.socket.authenticate.SocketUserPrincipal;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WalkController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CrewWalkCacheRepository crewWalkCacheRepository;
    private final CowalkCacheRepository cowalkCacheRepository;

    public WalkController(SimpMessagingTemplate simpMessagingTemplate, CrewWalkCacheRepository crewWalkCacheRepository, CowalkCacheRepository cowalkCacheRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.crewWalkCacheRepository = crewWalkCacheRepository;
        this.cowalkCacheRepository = cowalkCacheRepository;
    }

    private static String getCrewCountDestination(Long crewId) {
        return "/topic/walk/crew/" + crewId + "/count";
    }

    private static String getCrewDetailDestination(Long crewId) {
        return "/topic/walk/crew/" + crewId + "/detail";
    }

    private static String getCowalkCountDestination(Long cowalkId) {
        return "/topic/walk/cowalk/" + cowalkId + "/count";
    }

    @MessageMapping("/walk/ongoing")
    public void ongoingWalk(
            @Payload OngoingWalkRequest payload,
            Principal principal
    ) {
        SocketUserPrincipal user = (SocketUserPrincipal) principal;

        for (Long crewId : payload.crewIds()) {
            crewWalkCacheRepository.saveCrewMemberWalk(crewId, user.getMemberId(), user.getNickname(),
                    payload.distanceMeter(), payload.timeMin());
            List<WalkData> walkDataList = crewWalkCacheRepository.getAllCrewMemberWalk(crewId);
            simpMessagingTemplate.convertAndSend(getCrewCountDestination(crewId), new CountResponse(walkDataList.size()));
            simpMessagingTemplate.convertAndSend(getCrewDetailDestination(crewId), walkDataList);
        }
    }

    @MessageMapping("/walk/end")
    public void endWalk(
            @Payload EndWalkRequest payload,
            Principal principal
    ) {
        SocketUserPrincipal user = (SocketUserPrincipal) principal;

        for (Long crewId : payload.crewIds()) {
            crewWalkCacheRepository.deleteCrewMemberWalk(crewId, user.getMemberId());
            List<WalkData> walkDataList = crewWalkCacheRepository.getAllCrewMemberWalk(crewId);
            simpMessagingTemplate.convertAndSend(getCrewCountDestination(crewId), new CountResponse(walkDataList.size()));
            simpMessagingTemplate.convertAndSend(getCrewDetailDestination(crewId), walkDataList);
        }
    }

    @MessageMapping("/walk/cowalk/{cowalkId}/ongoing")
    public void ongoingCowalk(
            @DestinationVariable("cowalkId") Long cowalkId,
            Principal principal
    ) {
        SocketUserPrincipal user = (SocketUserPrincipal) principal;
        cowalkCacheRepository.saveCowalker(cowalkId, user.getMemberId());
        int count = cowalkCacheRepository.countCowalker(cowalkId);
        simpMessagingTemplate.convertAndSend(getCowalkCountDestination(cowalkId), new CountResponse(count));
    }

    @MessageMapping("/walk/cowalk/{cowalkId}/end")
    public void endCowalk(
            @DestinationVariable("cowalkId") Long cowalkId,
            Principal principal
    ) {
        SocketUserPrincipal user = (SocketUserPrincipal) principal;
        cowalkCacheRepository.deleteCowalker(cowalkId, user.getMemberId());
        int count = cowalkCacheRepository.countCowalker(cowalkId);
        simpMessagingTemplate.convertAndSend(getCowalkCountDestination(cowalkId), new CountResponse(count));
    }
}
