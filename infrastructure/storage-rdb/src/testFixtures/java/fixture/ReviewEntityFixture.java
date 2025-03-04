package fixture;

import com.dongsan.core.domains.review.Rating;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.review.ReviewEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;

import com.dongsan.rdb.domains.walkway.WalkwayHistoryEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ReviewEntityFixture {
    private static final Rating RATING = Rating.FIVE;
    private static final String CONTENT = "리뷰 내용";

    public static ReviewEntity createReview(MemberEntity memberEntity, WalkwayEntity walkwayEntity, WalkwayHistoryEntity walkwayHistory){
        return new ReviewEntity(RATING, CONTENT, memberEntity, walkwayEntity, walkwayHistory);
    }

    public static ReviewEntity createReview(MemberEntity memberEntity, WalkwayEntity walkwayEntity, WalkwayHistoryEntity walkwayHistory, Rating rating, String content){
        return new ReviewEntity(rating, content, memberEntity, walkwayEntity, walkwayHistory);
    }
}
