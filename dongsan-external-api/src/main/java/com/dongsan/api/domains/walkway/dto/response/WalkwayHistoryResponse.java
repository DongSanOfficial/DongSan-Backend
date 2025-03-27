package com.dongsan.api.domains.walkway.dto.response;

public record WalkwayHistoryResponse(
	Long walkwayHistoryId,
	Boolean canReview
) {
}
