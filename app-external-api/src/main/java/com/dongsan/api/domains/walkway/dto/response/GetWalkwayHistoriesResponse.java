package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.core.domains.walkway.WalkwayHistory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GetWalkwayHistoriesResponse(
        List<CanReviewWalkwayHistory> walkwayHistories
) {
    public static GetWalkwayHistoriesResponse from(List<WalkwayHistory> walkwayHistories) {
        return new GetWalkwayHistoriesResponse(
                walkwayHistories.stream()
                        .map(CanReviewWalkwayHistory::new)
                        .toList()
        );
    }

    public record CanReviewWalkwayHistory(
            Long walkwayId,
            String name,
            LocalDate date,
            Double distance,
            String courseImageUrl,
            Integer time,
            String memo,
            Integer likeCount,
            Integer reviewCount,
            Double rating,
            List<String> hashtags,
            Long walkwayHistoryId,
            LocalDateTime walkwayHistoryDate,
            Integer walkwayHistoryTime,
            Double walkwayHistoryDistance
    ) {
        public CanReviewWalkwayHistory(WalkwayHistory walkwayHistory) {
            this(
                    walkwayHistory.walkway().walkwayId(),
                    walkwayHistory.walkway().name(),
                    walkwayHistory.walkway().createdAt().toLocalDate(),
                    walkwayHistory.walkway().courseInfo().distance(),
                    walkwayHistory.walkway().courseInfo().courseImageUrl(),
                    walkwayHistory.walkway().courseInfo().time(),
                    walkwayHistory.walkway().memo(),
                    walkwayHistory.walkway().stat().likeCount(),
                    walkwayHistory.walkway().stat().reviewCount(),
                    walkwayHistory.walkway().stat().rating(),
                    walkwayHistory.walkway().hashtags(),
                    walkwayHistory.id(),
                    walkwayHistory.createdAt(),
                    walkwayHistory.time(),
                    walkwayHistory.distance()
            );
        }
    }
}
