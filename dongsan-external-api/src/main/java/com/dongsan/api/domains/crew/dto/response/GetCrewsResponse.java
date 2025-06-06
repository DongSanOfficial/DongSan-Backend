package com.dongsan.api.domains.crew.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewMember;

public record GetCrewsResponse(
        Long crewId,
        String name,
        String description,
        String rule,
        String visibility,
        boolean limitEnable,
        Integer memberLimit,
        int memberCount,
        String crewImageUrl,
        LocalDate createdAt,
        boolean isJoined,
        boolean isManager
) {

    public static List<GetCrewsResponse> from(
            List<Crew> crewList,
            Map<Long, CrewMember> crewMemberMap,
            Map<Long, Integer> memberCountMap
    ) {
        return crewList.stream()
                .map(crew -> {
                    CrewMember crewMember = crewMemberMap.get(crew.getId());

                    return new GetCrewsResponse(
                            crew.getId(),
                            crew.getName(),
                            crew.getDescription(),
                            crew.getRule(),
                            crew.getCrewType(),
                            crew.isLimitedCrew(),
                            crew.getMemberLimit(),
                            memberCountMap.getOrDefault(crew.getId(), 0),
                            crew.getCrewImageUrl(),
                            crew.getCreatedAt().toLocalDate(),
                            crewMember != null,
                            crewMember != null && crewMember.isManager()
                    );
                })
                .toList();
    }
}
