package com.dongsan.rdb.domains.walkway.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Embeddable
public class WalkwayGeometry {
    @Column(nullable = false)
    private Point startLocation;

    @Column(nullable = false)
    private Point endLocation;

    @Column(nullable = false)
    private LineString course;

    private String courseImageUrl;

    protected WalkwayGeometry() {
    }

    public WalkwayGeometry(Point startLocation, Point endLocation, LineString course, String courseImageUrl) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.course = course;
        this.courseImageUrl = courseImageUrl;
    }
}
