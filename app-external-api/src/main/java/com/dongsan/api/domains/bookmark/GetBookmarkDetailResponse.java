package com.dongsan.api.domains.bookmark;

import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.time.LocalDateTime;
import java.util.List;

public record GetBookmarkDetailResponse(
        List<WalkwayInfo> walkways,
        boolean hasNext
) {
    public GetBookmarkDetailResponse(CursorPagingResponse<MarkedWalkway> response){
        this(
                response.data().stream().map(WalkwayInfo::new).toList(),
                response.hasNext()
        );
    }

    public record WalkwayInfo(
            Long walkwayId,
            String name,
            LocalDateTime date,
            Double distance,
            List<String> hashtags,
            String courseImageUrl
    ){
        WalkwayInfo(MarkedWalkway walkway){
            this(
                    walkway.walkwayId(),
                    walkway.name(),
                    walkway.includedAt(),  // 북마크 추가 시간
                    walkway.distance(),
                    walkway.hashtags(),
                    walkway.courseImageUrl()
            );
        }
    }
}
