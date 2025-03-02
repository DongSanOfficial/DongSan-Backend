package com.dongsan.core.domains.walkway;

import java.util.List;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public record CreateWalkway(
        String name,
        Double distance,
        Integer time,
        ExposeLevel exposeLevel,
        Point startLocation,
        Point endLocation,
        String memo,
        LineString course,
        String courseImageUrl,
        List<String> hashtags,
        Long memberId
) {
}
