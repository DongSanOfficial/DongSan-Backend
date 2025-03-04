package com.dongsan.core.domains.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import review.ReviewFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewReader Unit Test")
class ReviewReaderTest {
    @InjectMocks
    private ReviewReader reviewReader;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GetReviewsFactory getReviewsFactory;
    @Mock
    private GetReviews getReviews;

    @Nested
    @DisplayName("getReview 메소드는")
    class Describe_getReview{
        @Test
        @DisplayName("리뷰가 존재하면 리뷰 목록을 반환한다.")
        void it_returns_review(){
            // given
            Long reviewId = 1L;
            Long memberId = 1L;
            Long walkwayId = 1L;
            Review review = ReviewFixture.createReviewWithId(reviewId, memberId, walkwayId);

            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

            // when
            Review result = reviewReader.getReview(reviewId);

            // then
            assertThat(result.reviewId()).isEqualTo(reviewId);
        }
    }

    @Nested
    @DisplayName("getUserReviews 메서드는")
    class Describe_getUserReviews {
        @Test
        @DisplayName("회원의 리뷰 목록을 반환 한다.")
        void it_returns_review_list() {
            // given
            int size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            Long memberId = 1L;
            Long walkwayId = 1L;
            List<Review> reviews = List.of(ReviewFixture.createReview(memberId, walkwayId));

            when(reviewRepository.getUserReviews(size, lastCreatedAt, memberId)).thenReturn(reviews);

            // when
            List<Review> result = reviewReader.getUserReviews(size, lastCreatedAt, memberId);

            // then
            assertThat(result).hasSize(reviews.size());
        }
    }

    @Nested
    @DisplayName("existsByReviewId 메소드는")
    class Describe_existsByReviewId{
        @Test
        @DisplayName("리뷰가 존재하면 true를 반환한다.")
        void it_returns_true_when_review_exists() {
            // given
            Long reviewId = 1L;
            when(reviewRepository.existsById(reviewId)).thenReturn(true);

            // when
            boolean result = reviewReader.existsByReviewId(reviewId);

            // then
            assertThat(result).isTrue();
            verify(reviewRepository).existsById(reviewId);
        }

        @Test
        @DisplayName("리뷰가 존재하지 않으면 false를 반환한다.")
        void it_returns_false_when_review_does_not_exist() {
            // given
            Long reviewId = 1L;
            when(reviewRepository.existsById(reviewId)).thenReturn(false);

            // when
            boolean result = reviewReader.existsByReviewId(reviewId);

            // then
            assertThat(result).isFalse();
            verify(reviewRepository).existsById(reviewId);
        }
    }

    @Nested
    @DisplayName("getWalkwaysRating 메서드는")
    class Describe_getWalkwaysRating {
        @Test
        @DisplayName("별점 별 리뷰 수를 반환 한다.")
        void it_returns_review_count() {
            // given
            Long walkwayId = 1L;
            Map<Rating, Long> reviewCount = new EnumMap<>(Rating.class);
            Rating rating = Rating.FIVE;
            Long ratingCount = 10L;
            reviewCount.put(rating, ratingCount);

            when(reviewRepository.getWalkwayRating(walkwayId)).thenReturn(reviewCount);

            // when
            Map<Rating, Long> result = reviewReader.getWalkwaysRating(walkwayId);

            // then
            assertThat(result).containsEntry(rating, ratingCount);
        }
    }

    @Nested
    @DisplayName("getWalkwayReviews 메서드는")
    class Describe_getWalkwayReviews {
        @Test
        @DisplayName("리뷰를 최신순으로 반환한다.")
        void it_returns_reviews() {
            // Given
            Integer size = 5;
            ReviewSort sort = ReviewSort.RATING;
            List<Review> reviews = List.of();
            Long memberId = 1L;
            Long walkwayId = 1L;
            Review review = ReviewFixture.createReview(memberId, walkwayId);

            when(getReviewsFactory.getService(sort)).thenReturn(getReviews);
            when(getReviews.search(size, review, walkwayId)).thenReturn(reviews);

            // When
            List<Review> result = reviewReader.getWalkwayReviews(size, review, walkwayId, sort);

            // Then
            assertThat(result).isEqualTo(reviews);
        }
    }

}