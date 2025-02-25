package fixture;

import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.LikedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;

public class LikedWalkwayFixture {

    public static LikedWalkwayEntity createLikedWalkway(MemberEntity memberEntity, WalkwayEntity walkwayEntity) {
        return new LikedWalkwayEntity(memberEntity, walkwayEntity);
    }

}
