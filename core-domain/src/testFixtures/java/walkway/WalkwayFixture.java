package walkway;

import com.dongsan.core.domains.walkway.CourseInfo;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.core.domains.walkway.Stat;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.support.util.Author;
import java.time.LocalDateTime;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayFixture {

    private static final Long ID = 1L;
    private static final Long MEMBER_ID = 1L;
    private static final String NAME = "WALKWAY NAME";
    private static final String MEMO = "WALKWAY MEMO";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final Integer LIKE_COUNT = 10;
    private static final Integer REVIEW_COUNT = 5;
    private static final Double RATING = 3.8;
    private static final List<String> HASHTAGS = List.of("tag1", "tag2", "tag2");
    private static final Double DISTANCE = 10.0;
    private static final Integer TIME = 10;
    private static final String IMAGE_URL = "TEST URL";

    public static Walkway createWalkway() {
        return new Walkway(ID, NAME, CREATED_AT, MEMO, createStat(), HASHTAGS, createCourseInfo(), new Author(MEMBER_ID), ExposeLevel.PUBLIC);
    }

    public static Walkway createWalkwayPrivate() {
        return new Walkway(ID, NAME, CREATED_AT, MEMO, createStat(), HASHTAGS, createCourseInfo(), new Author(MEMBER_ID), ExposeLevel.PRIVATE);
    }

    public static Walkway createWalkwayWithId(Long walkwayId) {
        return new Walkway(walkwayId, NAME, CREATED_AT, MEMO, createStat(), HASHTAGS, createCourseInfo(), new Author(MEMBER_ID), ExposeLevel.PUBLIC);
    }

    public static WalkwayHistory createWalkwayHistory() {
        return new WalkwayHistory(ID, MEMBER_ID, createWalkway(), 1.0, 10, true, CREATED_AT);
    }

    public static WalkwayHistory createWalkwayHistory(Double distance) {
        return new WalkwayHistory(ID, MEMBER_ID, createWalkway(), distance, 10, true, CREATED_AT);
    }

    private static Stat createStat() {
        return new Stat(LIKE_COUNT, REVIEW_COUNT, RATING);
    }

    private static CourseInfo createCourseInfo() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point startLocation = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
        Point endLocation = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
        Coordinate[] coordinates = {new Coordinate(0.0, 0.0), new Coordinate(0.0, 0.0)};
        LineString course = geometryFactory.createLineString(coordinates);
        return new CourseInfo(DISTANCE, TIME, startLocation, endLocation, course, IMAGE_URL);
    }
}
