package fixture;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.review.ReviewEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ReviewEntityFixture {
    private static final Integer RATING = 5;
    private static final String CONTENT = "리뷰 내용";

    public static ReviewEntity createReview(MemberEntity memberEntity, WalkwayEntity walkwayEntity){
        return ReviewEntity.builder()
                .member(memberEntity)
                .walkway(walkwayEntity)
                .rating(RATING)
                .content(CONTENT)
                .build();

    }

    public static ReviewEntity createReview(MemberEntity memberEntity, WalkwayEntity walkwayEntity, Integer rating, String content){
        return ReviewEntity.builder()
                .member(memberEntity)
                .walkway(walkwayEntity)
                .rating(rating)
                .content(content)
                .build();
    }

    public static ReviewEntity createReviewWithId(Long id, MemberEntity memberEntity, WalkwayEntity walkwayEntity){
        ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity);
        reflectId(id, reviewEntity);
        reflectCreatedAt(LocalDateTime.now(), reviewEntity);
        return reviewEntity;
    }

    public static ReviewEntity createReviewWithId(Long id, MemberEntity memberEntity, WalkwayEntity walkwayEntity, Integer rating, String content){
        ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity, rating, content);
        reflectId(id, reviewEntity);
        reflectCreatedAt(LocalDateTime.now(), reviewEntity);
        return reviewEntity;
    }

    private static void reflectId(Long id, ReviewEntity reviewEntity){
        try {
            Field idField = ReviewEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reviewEntity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, ReviewEntity reviewEntity){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(reviewEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
