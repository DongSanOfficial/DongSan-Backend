package com.dongsan.api.domains.cowalk.dto.response;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record GetMyCowalkResponse(
        Long cowalkId,
        LocalDate date,
        LocalTime time
) {
    public static List<GetMyCowalkResponse> from(List<CowalkPost> cowalkPostList) {
        return cowalkPostList.stream()
                .map(c -> new GetMyCowalkResponse(c.getId(), c.getDate(), c.getTime()))
                .toList();
    }
}
