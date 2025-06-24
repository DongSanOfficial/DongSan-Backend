package com.dongsan.socket;

import java.util.List;

public record EndWalkRequest(
        List<Long> crewIds
) {
}
