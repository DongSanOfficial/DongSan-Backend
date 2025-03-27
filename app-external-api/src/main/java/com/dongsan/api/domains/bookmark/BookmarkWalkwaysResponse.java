package com.dongsan.api.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;

import com.dongsan.core.domains.bookmark.MarkedWalkway;

public record BookmarkWalkwaysResponse(
	Long walkwayId,
	String name,
	LocalDateTime date,
	Double distance,
	List<String> hashtags,
	String courseImageUrl,
	int likeCount,
	int reviewCount,
	double rating

) {
	BookmarkWalkwaysResponse(MarkedWalkway walkway) {
		this(
			walkway.walkwayId(),
			walkway.name(),
			walkway.includedAt(),  // 북마크 추가 시간
			walkway.distance(),
			walkway.hashtags(),
			walkway.courseImageUrl(),
			walkway.likeCount(),
			walkway.reviewCount(),
			walkway.rating()
		);
	}

	public static List<BookmarkWalkwaysResponse> from(List<MarkedWalkway> walkways) {
		return walkways.stream()
			.map(BookmarkWalkwaysResponse::new)
			.toList();
	}
}
