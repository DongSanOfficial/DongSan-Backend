package com.dongsan.core.domains.review;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayHistoryValidator;
import com.dongsan.core.domains.walkway.WalkwayReader;
import com.dongsan.core.domains.walkway.WalkwayValidator;
import com.dongsan.core.domains.walkway.WalkwayWriter;
import com.dongsan.core.support.error.CoreException;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;

import review.ReviewFixture;
import walkway.WalkwayFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Unit Test")
class ReviewServiceTest {
	@Mock
	WalkwayReader walkwayReader;
	@Mock
	WalkwayWriter walkwayWriter;
	@Mock
	ReviewWriter reviewWriter;
	@Mock
	ReviewReader reviewReader;
	@Mock
	WalkwayHistoryValidator walkwayHistoryValidator;
	@Mock
	WalkwayValidator walkwayValidator;
	@InjectMocks
	ReviewService reviewService;

	@Nested
	@DisplayName("createReview 메서드는")
	class Describe_createReview {
		@Test
		@DisplayName("리뷰를 등록하고 DTO를 반환한다.")
		void it_returns_responseDTO() {
			// Given
			Long memberId = 1L;
			Long walkwayId = 1L;
			Long reviewId = 1L;
			Long walkwayHistoryId = 1L;
			Rating rating = Rating.FIVE;
			CreateReview createReview = new CreateReview(memberId, walkwayId, walkwayHistoryId, rating, "content");
			Map<Rating, Long> ratingCounts = new EnumMap<>(Rating.class);

			WalkwayHistory walkwayHistory = WalkwayFixture.createWalkwayHistory();

			when(walkwayReader.getWalkwayHistory(walkwayHistoryId)).thenReturn(walkwayHistory);
			when(reviewWriter.createReview(createReview)).thenReturn(reviewId);
			when(reviewReader.getWalkwaysRating(walkwayId)).thenReturn(ratingCounts);

			// When
			Long result = reviewService.createReview(createReview);

			// Then
			assertThat(result).isEqualTo(reviewId);
		}
	}

	@Nested
	@DisplayName("getWalkwayReviews 메서드는")
	class Describe_getWalkwayReviews {
		@Test
		@DisplayName("type이 rating이면 리뷰를 별점순으로 반환한다.")
		void it_returns_review_list_rating() {
			// Given
			int size = 5;
			Long walkwayId = 1L;
			Long memberId = 1L;
			String type = "rating";
			CursorRequest cursorRequest = new CursorRequest(null, size);

			Walkway walkway = WalkwayFixture.createWalkway();
			ReviewSort sort = ReviewSort.typeOf(type);

			List<Review> reviews = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				reviews.add(ReviewFixture.createReview(memberId, walkwayId));
			}

			when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);
			when(reviewReader.getWalkwayReviews(cursorRequest.size() + 1, null, walkwayId, sort)).thenReturn(reviews);

			// When
			PagingResponse<Review> result = reviewService.getWalkwayReviews(type, walkwayId, memberId, cursorRequest);

			// Then
			assertThat(result.data()).hasSize(size);
		}

		@Test
		@DisplayName("type이 latest이면 리뷰를 시간으로 반환한다.")
		void it_returns_review_list_latest() {
			// Given
			int size = 5;
			Long walkwayId = 1L;
			Long memberId = 1L;
			String type = "latest";
			CursorRequest cursorRequest = new CursorRequest(null, size);

			Walkway walkway = WalkwayFixture.createWalkway();
			ReviewSort sort = ReviewSort.typeOf(type);

			List<Review> reviews = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				reviews.add(ReviewFixture.createReview(memberId, walkwayId));
			}

			when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);
			when(reviewReader.getWalkwayReviews(cursorRequest.size() + 1, null, walkwayId, sort)).thenReturn(reviews);

			// When
			PagingResponse<Review> result = reviewService.getWalkwayReviews(type, walkwayId, memberId, cursorRequest);

			// Then
			assertThat(result.data()).hasSize(size);
		}

		@Test
		@DisplayName("type이 latest나 rating이 아니면 예외처리한다.")
		void it_returns_exception() {
			// Given
			Integer size = 5;
			Long walkwayId = 1L;
			Long memberId = 1L;
			String type = "크아아아ㅏ아아아악";
			CursorRequest cursorRequest = new CursorRequest(null, size);

			Walkway walkway = WalkwayFixture.createWalkway();

			when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);

			// When & Then
			assertThatThrownBy(() -> reviewService.getWalkwayReviews(type, walkwayId, memberId, cursorRequest))
				.isInstanceOf(CoreException.class);
		}
	}

	@Nested
	@DisplayName("getWalkwayRating 메서드는")
	class Describe_getWalkwayRating {
		@Test
		@DisplayName("산책로가 존재하면 산책로의 별점 내용을 반환한다.")
		void it_returns_walkway_rating_info() {
			// Given
			Long walkwayId = 1L;
			Long memberId = 1L;
			Walkway walkway = WalkwayFixture.createWalkway();
			Long count = 20L;

			Map<Rating, Long> ratingCounts = new EnumMap<>(Rating.class);
			for (int i = 1; i <= 5; i++) {
				ratingCounts.put(Rating.numOf(i), count);
			}

			when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);
			when(reviewReader.getWalkwaysRating(walkwayId)).thenReturn(ratingCounts);

			// When
			Map<Rating, Long> result = reviewService.getWalkwayRating(walkwayId, memberId);

			// Then
			assertThat(result).containsEntry(Rating.FIVE, count)
				.containsEntry(Rating.FOUR, count)
				.containsEntry(Rating.THREE, count)
				.containsEntry(Rating.TWO, count)
				.containsEntry(Rating.ONE, count);
		}
	}
}
