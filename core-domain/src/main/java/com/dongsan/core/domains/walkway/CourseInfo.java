package com.dongsan.core.domains.walkway;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public record CourseInfo(
        Double distance,
        Integer time,
        Point startLocation,
        Point endLocation,
        LineString course,
        String courseImageUrl
) {
}
