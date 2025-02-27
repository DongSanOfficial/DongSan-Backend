//package com.dongsan.domains.user.usecase;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static fixture.ReflectFixture.reflectCreatedAt;
//import static fixture.ReflectFixture.reflectField;
//import static review.ReviewFixture.createReview;
//import static fixture.WalkwayFixture.createWalkwayWithId;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.core.domains.review.UserReviewService;
//import com.dongsan.domains.review.entity.Review;
//import com.dongsan.core.domains.review.ReviewReader;
//import com.dongsan.core.domains.review.GetReviewResponse;
//import com.dongsan.core.domains.review.GetReviewResponse.ReviewInfo;
//import com.dongsan.domains.walkway.entity.Walkway;
//import review.ReviewFixture;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("UserReviewUseCase Unit Test")
//class UserReviewServiceTest {
//    @InjectMocks
//    UserReviewService userReviewService;
//    @Mock
//    ReviewReader reviewReader;
//
//    @Nested
//    @DisplayName("getReviews 메소드는")
//    class Describe_getReviews{
//        @Test
//        @DisplayName("리뷰가 존재하면 리뷰를 DTO로 변환한다.")
//        void it_returns_responseDTO(){
//            // given
//            Integer size = 5;
//            Long lastId = 2L;
//            Long memberId = 1L;
//
//            Review review = createReview(null, null);
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 23, 11, 11);
//            reflectField(review, "id", lastId);
//            reflectCreatedAt(review, lastCreatedAt);
//
//            Member member = createMemberWithId(memberId);
//            Walkway walkway = createWalkwayWithId(1L, member);
//            List<Review> reviews = IntStream.range(0, 5)
//                    .mapToObj(index ->
//                            ReviewFixture.createReviewWithId((long) (index+1), member, walkway)
//                    ).toList();
//
//            when(reviewReader.getReview(lastId)).thenReturn(review);
//            when(reviewReader.getReviews(size+1, lastCreatedAt, memberId)).thenReturn(reviews);
//
//            // when
//            GetReviewResponse result = userReviewService.getReviews(size, lastId, memberId);
//
//            // then
//            assertThat(result.reviews()).hasSameSizeAs(reviews);
//            for(int i=0; i<result.reviews().size(); i++){
//                ReviewInfo reviewInfo = result.reviews().get(i);
//                assertThat(reviewInfo.reviewId()).isEqualTo(reviews.get(i).getId());
//                assertThat(reviewInfo.walkwayId()).isEqualTo(reviews.get(i).getWalkway().getId());
//            }
//        }
//    }
//
//
//}