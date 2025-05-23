package com.dongsan.rdb.domains.walkway.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Embeddable
public class WalkwayGeometry {
    private static final int SRID_WGS84 = 4326;  // static 이어서 JPA 매핑 X

    @Column(nullable = false)
    private Point startLocation;

    @Column(nullable = false)
    private Point endLocation;

    @Column(nullable = false)
    private LineString course;

    private String courseImageUrl;

    protected WalkwayGeometry() {
    }

    public WalkwayGeometry(LineString course, String courseImageUrl) {
        Point startLocation = course.getStartPoint();
        Point endLocation = course.getEndPoint();

        course.setSRID(SRID_WGS84);
        startLocation.setSRID(SRID_WGS84);
        endLocation.setSRID(SRID_WGS84);

        this.course = course;
        this.courseImageUrl = courseImageUrl;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public LineString getCourse() {
        return course;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }
}
