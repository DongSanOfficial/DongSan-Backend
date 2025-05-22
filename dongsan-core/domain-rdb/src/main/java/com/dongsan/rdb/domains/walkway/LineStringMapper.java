package com.dongsan.rdb.domains.walkway;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

public class LineStringMapper {
    private LineStringMapper() {
    }

    public static List<WalkwayCoordinate> toList(LineString lineString) {
        List<WalkwayCoordinate> course = new ArrayList<>();
        for (Coordinate coordinate : lineString.getCoordinates()) {
            WalkwayCoordinate point = new WalkwayCoordinate(coordinate.getY(), coordinate.getX());
            course.add(point);
        }
        return course;
    }

    public static LineString toLineString(List<WalkwayCoordinate> course) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinateList = new Coordinate[course.size()];

        for (int i = 0; i < course.size(); i++) {
            WalkwayCoordinate point = course.get(i);
            coordinateList[i] = new Coordinate(point.longitude(), point.latitude());
        }

        return geometryFactory.createLineString(coordinateList);
    }
}
