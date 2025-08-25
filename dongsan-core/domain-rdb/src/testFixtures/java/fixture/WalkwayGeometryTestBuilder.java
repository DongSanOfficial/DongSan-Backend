package fixture;

import com.dongsan.domain.domains.walkway.domain.WalkwayGeometry;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class WalkwayGeometryTestBuilder {
    private LineString course;
    private String courseImageUrl = "test image url";

    public WalkwayGeometryTestBuilder() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = {new Coordinate(127.0, 37.0), new Coordinate(127.1, 37.1)};
        this.course = geometryFactory.createLineString(coordinates);
    }

    public WalkwayGeometryTestBuilder course(LineString course) {
        this.course = course;
        return this;
    }

    public WalkwayGeometryTestBuilder courseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
        return this;
    }

    public WalkwayGeometry build() {
        return new WalkwayGeometry(course, courseImageUrl);
    }
}
