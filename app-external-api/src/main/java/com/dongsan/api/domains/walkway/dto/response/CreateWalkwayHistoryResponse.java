package com.dongsan.api.domains.walkway.dto.response;

public record CreateWalkwayHistoryResponse(
        Long walkwayHistoryId,
        Boolean canReview
) {
}
