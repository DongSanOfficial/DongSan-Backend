//package fixture;
//
//import com.dongsan.domains.common.entity.BaseEntity;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.walkway.entity.Walkway;
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//public class WalkwayHistoryFixture {
//    private static final Double DISTANCE = 1.9;
//    private static final Integer TIME = 600;
//
//    public static com.dongsan.domains.walkway.entity.WalkwayHistoryEntity createWalkwayHistory(Member member, Walkway walkway) {
//        return com.dongsan.domains.walkway.entity.WalkwayHistoryEntity.builder()
//                .member(member)
//                .walkway(walkway)
//                .distance(DISTANCE)
//                .time(TIME)
//                .build();
//    }
//
//    public static com.dongsan.domains.walkway.entity.WalkwayHistoryEntity createWalkwayHistory(Member member, Walkway walkway, Double distance, Integer time) {
//        return com.dongsan.domains.walkway.entity.WalkwayHistoryEntity.builder()
//                .member(member)
//                .walkway(walkway)
//                .distance(distance)
//                .time(time)
//                .build();
//    }
//
//    public static com.dongsan.domains.walkway.entity.WalkwayHistoryEntity createWalkwayHistoryWithId(Long id, Member member, Walkway walkway){
//        com.dongsan.domains.walkway.entity.WalkwayHistoryEntity walkwayHistory = createWalkwayHistory(member, walkway);
//        reflectId(id, walkwayHistory);
//        reflectCreatedAt(LocalDateTime.now(), walkwayHistory);
//        return walkwayHistory;
//    }
//
//    public static com.dongsan.domains.walkway.entity.WalkwayHistoryEntity createWalkwayHistoryWithId(Long id, Member member, Walkway walkway, Double distance, Integer time){
//        com.dongsan.domains.walkway.entity.WalkwayHistoryEntity walkwayHistory = createWalkwayHistory(member, walkway, distance, time);
//        reflectId(id, walkwayHistory);
//        reflectCreatedAt(LocalDateTime.now(), walkwayHistory);
//        return walkwayHistory;
//    }
//
//    private static void reflectId(Long id, com.dongsan.domains.walkway.entity.WalkwayHistoryEntity walkwayHistory){
//        try {
//            Field idField = com.dongsan.domains.walkway.entity.WalkwayHistoryEntity.class.getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(walkwayHistory, id);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void reflectCreatedAt(LocalDateTime createdAt, com.dongsan.domains.walkway.entity.WalkwayHistoryEntity walkwayHistory){
//        try {
//            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
//            createdAtField.setAccessible(true);
//            createdAtField.set(walkwayHistory, createdAt);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
