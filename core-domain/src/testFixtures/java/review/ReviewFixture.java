package review;

import com.dongsan.core.domains.review.Rating;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.ReviewedWalkway;
import com.dongsan.core.domains.review.Reviewer;
import java.time.LocalDateTime;

public class ReviewFixture {
    private static final Long ID = 1L;
    private static final Rating RATING = Rating.FIVE;
    private static final String CONTENT = "test content";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final String NICKNAME = "member";
    private static final String WALKWAY_NAME = "walkway";

    public static Review createReview(Long memberId, Long walkwayId) {
        return new Review(ID, createReviewer(memberId), createReviewWalkway(walkwayId), RATING, CONTENT, CREATED_AT);
    }

    public static Review createReviewWithId(Long id, Long memberId, Long walkwayId) {
        return new Review(id, createReviewer(memberId), createReviewWalkway(walkwayId), RATING, CONTENT, CREATED_AT);
    }

    private static Reviewer createReviewer(Long memberId) {
        return new Reviewer(memberId, NICKNAME);
    }

    private static ReviewedWalkway createReviewWalkway(Long walkwayId) {
        return new ReviewedWalkway(walkwayId, WALKWAY_NAME);
    }
}
