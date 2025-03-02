package com.dongsan.core.domains.walkway;

import java.util.List;

public record UpdateWalkway(
        Long walkwayId,
        String name,
        String memo,
        ExposeLevel exposeLevel,
        List<String> hashtags
) {
}
