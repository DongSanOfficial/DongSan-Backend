package com.dongsan.api.domains.cowalk.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.dongsan.domain.domains.cowalk.domain.CapacityType;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.member.Member;

public record CowalkPostsResponse(
        Long cowalkId,
        String nickname,
        LocalDate createdDate,
        LocalDate date,
        LocalTime time,
        Boolean limitEnable,
        Integer memberCount,
        Integer memberLimit,
        Integer commentCount
) {
    public CowalkPostsResponse(CowalkPost cowalkPost, Integer memberCount, Integer commentCount, Member member) {
        this(
                cowalkPost.getId(),
                member.getNickname(),
                cowalkPost.getCreatedAt().toLocalDate(),
                cowalkPost.getDate(),
                cowalkPost.getTime(),
                cowalkPost.getCapacityType().equals(CapacityType.LIMITED),
                memberCount,
                cowalkPost.getCapacity(),
                commentCount
        );
    }

    public static List<CowalkPostsResponse> from(
            List<CowalkPost> cowalkPosts,
            Map<Long, Member> memberMap,
            Map<Long, Integer> memberCountMap,
            Map<Long, Integer> commentCountMap
    ) {
        return cowalkPosts.stream()
                .map(cowalkPost -> new CowalkPostsResponse(
                                cowalkPost,
                                memberCountMap.getOrDefault(cowalkPost.getId(), 0),
                                commentCountMap.getOrDefault(cowalkPost.getId(), 0),
                                memberMap.get(cowalkPost.getMemberId())
                        )
                )
                .toList();
    }
}
