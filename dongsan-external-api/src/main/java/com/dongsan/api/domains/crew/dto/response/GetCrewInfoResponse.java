package com.dongsan.api.domains.crew.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.domains.member.Member;

public record GetCrewInfoResponse(
        String name,
        String description,
        String rule,
        String visibility,
        boolean limitEnable,
        int memberCount,
        Integer memberLimit,
        String crewImageUrl,
        LocalDate createdAt,
        WeeklyStatsResponse weeklyStats,
        boolean isJoined,
        Long crewImageId,
        String managerNickname
) {

    public GetCrewInfoResponse(Crew crew, int memberCount, CrewWeeklyStatistic crewWeeklyStat, boolean isJoined,
            Long crewImageId, Member manager) {
        this(
                crew.getName(),
                crew.getDescription(),
                crew.getRule(),
                crew.getCrewType(),
                crew.isLimitedCrew(),
                memberCount,
                crew.getMemberLimit(),
                crew.getCrewImageUrl(),
                crew.getCreatedAt().toLocalDate(),
                new WeeklyStatsResponse(crewWeeklyStat),
                isJoined,
                crewImageId,
                manager.getNickname()
        );
    }

    public record WeeklyStatsResponse(
            double distanceKm,
            double durationHour
    ) {

        public WeeklyStatsResponse(CrewWeeklyStatistic stat) {
            this(
                    roundToTwoDecimal(stat.distanceKm()),
                    roundToTwoDecimal(stat.durationSec() / 3600.0)
            );
        }

        private static double roundToTwoDecimal(double value) {
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
