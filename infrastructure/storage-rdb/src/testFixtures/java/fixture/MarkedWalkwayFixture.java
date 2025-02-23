package fixture;

import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
import com.dongsan.rdb.domains.bookmark.MarkedWalkwayEntity;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MarkedWalkwayFixture {

    public static MarkedWalkwayEntity createMarkedWalkway(WalkwayEntity walkwayEntity, BookmarkEntity bookmarkEntity){
        return MarkedWalkwayEntity.builder()
                .walkway(walkwayEntity)
                .bookmark(bookmarkEntity)
                .build();
    }


    public static MarkedWalkwayEntity createMarkedWalkwayWithId(WalkwayEntity walkwayEntity, BookmarkEntity bookmarkEntity, Long id){
        MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
        reflectId(id, markedWalkway);
        reflectCreatedAt(LocalDateTime.now(), markedWalkway);
        return markedWalkway;
    }


    private static void reflectId(Long id, MarkedWalkwayEntity markedWalkway){
        try {
            Field idField = MarkedWalkwayEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(markedWalkway, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, MarkedWalkwayEntity markedWalkway){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(markedWalkway, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
