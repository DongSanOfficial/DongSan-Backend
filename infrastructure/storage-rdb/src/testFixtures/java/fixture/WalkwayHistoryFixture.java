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
}
