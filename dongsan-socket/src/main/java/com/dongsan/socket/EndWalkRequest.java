package com.dongsan.socket;

import java.util.List;

public record EndWalkRequest(
        Long memberId,
        List<Long> crewIds
) {
}
