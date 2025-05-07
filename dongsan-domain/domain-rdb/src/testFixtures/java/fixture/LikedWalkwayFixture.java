package fixture;

import com.dongsan.rdb.domains.walkway.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rds.domains.member.Member;

public class LikedWalkwayFixture {

    public static LikedWalkwayEntity createLikedWalkway(Member member, WalkwayEntity walkwayEntity) {
        return new LikedWalkwayEntity(member, walkwayEntity);
    }

}
