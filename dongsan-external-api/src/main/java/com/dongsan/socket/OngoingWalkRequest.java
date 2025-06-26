package com.dongsan.socket;

import java.util.List;

public record OngoingWalkRequest(
        List<Long> crewIds,
        long distanceMeter,
        long timeMin
) {
}
