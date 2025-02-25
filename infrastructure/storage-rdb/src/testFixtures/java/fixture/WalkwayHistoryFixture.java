package fixture;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayHistoryEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class WalkwayHistoryFixture {
    private static final Double DISTANCE = 1.9;
    private static final Integer TIME = 600;

    public static WalkwayHistoryEntity createWalkwayHistory(MemberEntity member, WalkwayEntity walkway) {
        return new WalkwayHistoryEntity(member, walkway, DISTANCE, TIME);
    }

    public static WalkwayHistoryEntity createWalkwayHistory(MemberEntity member, WalkwayEntity walkway, Double distance, Integer time) {
        return new WalkwayHistoryEntity(member, walkway, distance, time);
    }

    public static WalkwayHistoryEntity createWalkwayHistoryWithId(Long id, MemberEntity member, WalkwayEntity walkway){
        WalkwayHistoryEntity walkwayHistory = createWalkwayHistory(member, walkway);
        reflectId(id, walkwayHistory);
        reflectCreatedAt(LocalDateTime.now(), walkwayHistory);
        return walkwayHistory;
    }

    public static WalkwayHistoryEntity createWalkwayHistoryWithId(Long id, MemberEntity member, WalkwayEntity walkway, Double distance, Integer time){
        WalkwayHistoryEntity walkwayHistory = createWalkwayHistory(member, walkway, distance, time);
        reflectId(id, walkwayHistory);
        reflectCreatedAt(LocalDateTime.now(), walkwayHistory);
        return walkwayHistory;
    }

    private static void reflectId(Long id, WalkwayHistoryEntity walkwayHistory){
        try {
            Field idField = WalkwayHistoryEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(walkwayHistory, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, WalkwayHistoryEntity walkwayHistory){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(walkwayHistory, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
