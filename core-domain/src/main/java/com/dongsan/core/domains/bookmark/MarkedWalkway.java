package com.dongsan.core.domains.bookmark;

import com.dongsan.core.domains.walkway.ExposeLevel;
import java.time.LocalDateTime;
import java.util.List;

public record MarkedWalkway(
        Long walkwayId,
        Long authorId,
        String name,
        LocalDateTime includedAt,
        Double distance,
        List<String> hashtags,
        String courseImageUrl,
        ExposeLevel exposeLevel
) {
}
