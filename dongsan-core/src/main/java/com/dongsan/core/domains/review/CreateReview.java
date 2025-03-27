package com.dongsan.core.domains.review;

public record CreateReview(
	Long memberId,
	Long walkwayId,
	Long walkwayHistoryId,
	Rating rating,
	String content
) {
}
