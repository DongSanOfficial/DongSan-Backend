package com.dongsan.api.domains.cowalk.dto.response;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;

import java.time.LocalDateTime;
import java.util.List;

public record GetMyCowalkResponse(
        Long cowalkId,
        LocalDateTime startedAt,
        LocalDateTime endedAt
) {
    public static List<GetMyCowalkResponse> from(List<CowalkPost> cowalkPostList) {
        return cowalkPostList.stream()
                .map(c -> new GetMyCowalkResponse(c.getId(), c.getStartedAt(), c.getEndedAt()))
                .toList();
    }
}
