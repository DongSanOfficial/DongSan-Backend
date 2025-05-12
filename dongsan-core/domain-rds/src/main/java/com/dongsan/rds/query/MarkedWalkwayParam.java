package com.dongsan.rds.query;

import java.time.LocalDateTime;
import java.util.List;

public record MarkedWalkwayParam(
        Long walkwayId,
        String name,
        Double distance,
        List<String> hashtags,
        String courseImageUrl,
        int likeCount,
        int reviewCount,
        double rating,
        LocalDateTime date  // 북마크 추가 시간
) {
}
