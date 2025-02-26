//package fixture;
//
//import com.dongsan.rdb.domains.common.entity.BaseEntity;
//import com.dongsan.rdb.domains.walkway.repository.HashtagEntity;
//import com.dongsan.rdb.domains.walkway.repository.HashtagWalkway;
//import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//public class HashtagWalkwayFixture {
//
//    private static final String NAME = "산책로 태그";
//
//    public static HashtagWalkway createHashtagWalkway(WalkwayEntity walkwayEntity, HashtagEntity hashtagEntity){
//        return HashtagWalkway.builder()
//                .walkway(walkwayEntity)
//                .hashtag(hashtagEntity)
//                .build();
//    }
//
//
//    public static HashtagWalkway createHashtagWalkwayWithId(WalkwayEntity walkwayEntity, HashtagEntity hashtagEntity, Long id){
//        HashtagWalkway hashtagWalkway = createHashtagWalkway(walkwayEntity, hashtagEntity);
//        reflectId(id, hashtagWalkway);
//        reflectCreatedAt(LocalDateTime.now(), hashtagWalkway);
//        return hashtagWalkway;
//    }
//
//
//    private static void reflectId(Long id, HashtagWalkway hashtagWalkway){
//        try {
//            Field idField = HashtagWalkway.class.getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(hashtagWalkway, id);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void reflectCreatedAt(LocalDateTime createdAt, HashtagWalkway hashtagWalkway){
//        try {
//            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
//            createdAtField.setAccessible(true);
//            createdAtField.set(hashtagWalkway, createdAt);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
