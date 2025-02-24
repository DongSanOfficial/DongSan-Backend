package fixture;

import static fixture.HashtagEntityFixture.createHashtag;
import static fixture.HashtagWalkwayFixture.createHashtagWalkway;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.repository.HashtagEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayEntityFixture {
    private static final String NAME = "Sample Walkway";
    private static final Double DISTANCE = 2.5;
    private static final Integer TIME = 30; // minutes
    private static final ExposeLevel EXPOSE_LEVEL = ExposeLevel.PUBLIC;
    private static final String MEMO = "A beautiful walkway.";
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final Point START_LOCATION = GEOMETRY_FACTORY.createPoint(new Coordinate(0.0, 0.0));
    private static final Point END_LOCATION = GEOMETRY_FACTORY.createPoint(new Coordinate(1.0, 1.0));
    private static final LineString COURSE = GEOMETRY_FACTORY.createLineString(new Coordinate[]{
            new Coordinate(0.0, 0.0),
            new Coordinate(1.0, 1.0),
            new Coordinate(2.0, 2.0)
    });

    private static final String COURSE_IMAGE_URL = "http://example.com/course-image.png";

    public static WalkwayEntity createWalkway(MemberEntity memberEntity) {
        WalkwayEntity walkWalk = WalkwayEntity.builder()
                .member(memberEntity)
                .name(NAME)
                .distance(DISTANCE)
                .time(TIME)
                .exposeLevel(EXPOSE_LEVEL)
                .startLocation(START_LOCATION)
                .endLocation(END_LOCATION)
                .memo(MEMO)
                .course(COURSE)
                .courseImageUrl(COURSE_IMAGE_URL)
                .build();
        for(int i=0; i<3; i++){
            HashtagEntity hashtagEntity = createHashtag();
            walkWalk.getHashtagWalkways().add(createHashtagWalkway(walkWalk, hashtagEntity));
        }

        return walkWalk;
    }

    public static WalkwayEntity createPrivateWalkway(MemberEntity memberEntity){
        WalkwayEntity walkWalk = WalkwayEntity.builder()
                .member(memberEntity)
                .name(NAME)
                .distance(DISTANCE)
                .time(TIME)
                .exposeLevel(ExposeLevel.PRIVATE)
                .startLocation(START_LOCATION)
                .endLocation(END_LOCATION)
                .memo(MEMO)
                .course(COURSE)
                .courseImageUrl(COURSE_IMAGE_URL)
                .build();
        for(int i=0; i<3; i++){
            HashtagEntity hashtagEntity = createHashtag();
            walkWalk.getHashtagWalkways().add(createHashtagWalkway(walkWalk, hashtagEntity));
        }

        return walkWalk;

    }

    public static WalkwayEntity createWalkwayWithId(Long id, MemberEntity memberEntity){
        WalkwayEntity walkwayEntity = createWalkway(memberEntity);
        reflectId(id, walkwayEntity);
        reflectCreatedAt(LocalDateTime.now(), walkwayEntity);
        return walkwayEntity;
    }

    private static void reflectId(Long id, WalkwayEntity walkwayEntity){
        try {
            Field idField = WalkwayEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(walkwayEntity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, WalkwayEntity walkwayEntity){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(walkwayEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
