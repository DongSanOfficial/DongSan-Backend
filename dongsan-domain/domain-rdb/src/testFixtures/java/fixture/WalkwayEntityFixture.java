//package fixture;
//
//import java.util.List;
//
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.LineString;
//import org.locationtech.jts.geom.Point;
//
//import com.dongsan.core.domains.walkway.ExposeLevel;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//
//public class WalkwayEntityFixture {
//	private static final String NAME = "Sample Walkway";
//	private static final Double DISTANCE = 2.5;
//	private static final Integer TIME = 30; // minutes
//	private static final ExposeLevel EXPOSE_LEVEL = ExposeLevel.PUBLIC;
//	private static final String MEMO = "A beautiful walkway.";
//	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
//	private static final Point START_LOCATION = createPoint(0.0, 0.0, 4326);
//	private static final Point END_LOCATION = createPoint(1.0, 1.0, 4326);
//	private static final LineString COURSE = createLineString(new Coordinate[] {
//		new Coordinate(0.0, 0.0),
//		new Coordinate(1.0, 1.0),
//		new Coordinate(2.0, 2.0)
//	}, 4326);
//	private static final String COURSE_IMAGE_URL = "http://example.com/course-image.png";
//	private static final List<String> HASHTAGS = List.of("happy", "silent");
//
//	public static WalkwayEntity createWalkway(MemberEntity memberEntity) {
//		return new WalkwayEntity(NAME, DISTANCE, TIME, EXPOSE_LEVEL, START_LOCATION,
//			END_LOCATION, MEMO, COURSE, COURSE_IMAGE_URL, memberEntity, HASHTAGS);
//	}
//
//	public static WalkwayEntity createPrivateWalkway(MemberEntity memberEntity) {
//		return new WalkwayEntity(NAME, DISTANCE, TIME, ExposeLevel.PRIVATE, START_LOCATION,
//			END_LOCATION, MEMO, COURSE, COURSE_IMAGE_URL, memberEntity, HASHTAGS);
//	}
//
//	private static Point createPoint(double x, double y, int srid) {
//		Point point = GEOMETRY_FACTORY.createPoint(new Coordinate(x, y));
//		point.setSRID(srid);
//		return point;
//	}
//
//	// LineString 생성 및 SRID 설정
//	private static LineString createLineString(Coordinate[] coordinates, int srid) {
//		LineString lineString = GEOMETRY_FACTORY.createLineString(coordinates);
//		lineString.setSRID(srid);
//		return lineString;
//	}
//}
