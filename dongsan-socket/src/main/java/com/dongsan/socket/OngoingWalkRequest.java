package com.dongsan.socket;

import java.util.List;

public record OngoingWalkRequest(
        Long memberId,
        String nickname,
        List<Long> crewIds,
        long distanceMeter,
        long timeMin
) {
}
