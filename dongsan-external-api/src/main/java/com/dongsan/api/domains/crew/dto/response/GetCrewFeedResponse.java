package com.dongsan.api.domains.crew.dto.response;

import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.support.paging.CursorResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record GetCrewFeedResponse(
        Long walkwayHistoryId,
        Long memberId,
        String nickname,
        LocalDate date,
        double distanceKm,
        int durationSec
) {
    public static List<GetCrewFeedResponse> from(CursorResponse<WalkwayLog> walkwayLogs, Map<Long, Member> memberMap) {
        return walkwayLogs.getData().stream()
                .map(walkwayLog -> {
                    Member member = memberMap.get(walkwayLog.getMemberId());
                    if (member == null) {
                        throw new IllegalStateException("Member not found");
                    }
                    return new GetCrewFeedResponse(
                            walkwayLog.getId(),
                            walkwayLog.getMemberId(),
                            member.getNickname(),
                            walkwayLog.getCreatedAt().toLocalDate(),
                            walkwayLog.getDistance() == null ? 0.0 : walkwayLog.getDistance(),
                            walkwayLog.getTime() == null ? 0 : walkwayLog.getTime()
                    );
                }).toList();
    }
}
