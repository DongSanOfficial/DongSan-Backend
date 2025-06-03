package com.dongsan.api.domains.crew.dto.response;

import com.dongsan.domain.domains.crew.domain.CrewMemberStatistic;
import com.dongsan.domain.domains.member.Member;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public record GetCrewMemberRankingResponse(
        Long memberId,
        String nickname,
        double distanceKm,
        double durationHour
) {

    // TODO : 이거 Util 로 빼기
    private static double roundToTwoDecimal(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<GetCrewMemberRankingResponse> from(List<CrewMemberStatistic> response, Map<Long, Member> memberMap) {
        return response.stream()
                .map(stat -> {
                    Member member = memberMap.get(stat.memberId());
                    if (member == null) {
                        throw new IllegalStateException("Member not found");
                    }
                    return new GetCrewMemberRankingResponse(
                            stat.memberId(),
                            member.getNickname(),
                            roundToTwoDecimal(stat.distanceKm()),
                            roundToTwoDecimal(stat.durationSec() / 3600.0)
                    );
                }).toList();
    }
}
