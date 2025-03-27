package com.dongsan.api.domains.review;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.dongsan.core.domains.review.Review;
import com.dongsan.core.support.format.TimeFormat;

public record WalkwayReviewsResponse(
	Long reviewId,
	String nickname,
	String date,
	String period,
	Integer rating,
	String content
) {
	public WalkwayReviewsResponse(Review review) {
		this(
			review.reviewId(),
			review.reviewer()
				.nickname(),
			review.createdAt()
				.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
			TimeFormat.formatTimeString(review.createdAt()),
			review.rating()
				.getNum(),
			review.content()
		);
	}

	public static List<WalkwayReviewsResponse> from(List<Review> reviews) {
		return reviews.stream()
			.map(WalkwayReviewsResponse::new)
			.toList();
	}
}
