package com.dongsan.domain.domains.crew.domain;

public record CrewMemberStatistic(
        Long memberId,
        double distanceKm,
        int durationSec
) {
}
