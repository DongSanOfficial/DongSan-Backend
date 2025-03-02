package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.core.domains.walkway.Walkway;
import java.time.LocalDate;
import java.util.List;

public record WalkwayListResponse(
        List<WalkwayResponse> walkways,
        boolean hasNext
) {
    public static WalkwayListResponse from(List<Walkway> walkways, boolean hasNext) {
        return new WalkwayListResponse(walkways.stream()
                .map(WalkwayResponse::new)
                .toList(),
                hasNext
        );
    }

    public record WalkwayResponse(
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
            List<String> hashtags
    ) {
        public WalkwayResponse(Walkway w) {
            this(
                    w.walkwayId(),
                    w.name(),
                    w.createdAt()
                            .toLocalDate(),
                    w.courseInfo().distance(),
                    w.courseInfo().courseImageUrl(),
                    w.courseInfo().time(),
                    w.memo(),
                    w.stat().likeCount(),
                    w.stat().reviewCount(),
                    w.stat().rating(),
                    w.hashtags()
            );
        }
    }
}