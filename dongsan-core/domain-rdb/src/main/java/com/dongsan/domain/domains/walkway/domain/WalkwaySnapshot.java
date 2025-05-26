package com.dongsan.domain.domains.walkway.domain;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

public record WalkwaySnapshot(
        Long id,
        Long memberId,
        String name,
        String memo,
        ExposeLevel exposeLevel,
        List<String> hashtags,
        double distance,
        int time,
        LineString course,
        String courseImageUrl,
        LocalDateTime createdAt,
        Point startLocation,
        Point endLocation
) {
}
