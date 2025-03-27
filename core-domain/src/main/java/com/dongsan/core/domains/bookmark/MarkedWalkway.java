package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;

import com.dongsan.core.domains.walkway.ExposeLevel;

public record MarkedWalkway(
	Long walkwayId,
	Long authorId,
	String name,
	LocalDateTime includedAt,
	Double distance,
	List<String> hashtags,
	String courseImageUrl,
	ExposeLevel exposeLevel,
	int likeCount,
	int reviewCount,
	double rating
) {
}
