package fixture;

import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.walkway.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;

public class LikedWalkwayFixture {

    public static LikedWalkwayEntity createLikedWalkway(Member member, WalkwayEntity walkwayEntity) {
        return new LikedWalkwayEntity(member, walkwayEntity);
    }

}
