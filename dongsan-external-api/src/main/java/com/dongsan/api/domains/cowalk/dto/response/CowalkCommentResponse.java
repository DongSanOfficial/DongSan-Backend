package com.dongsan.api.domains.cowalk.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dongsan.domain.domains.cowalk.domain.CowalkComment;
import com.dongsan.domain.domains.member.Member;

public record CowalkCommentResponse(
        Long commentId,
        String profileImageUrl,
        String nickname,
        LocalDate createDate,
        String content
) {
    public static List<CowalkCommentResponse> from(
            List<CowalkComment> comments,
            Map<Long, Member> memberMap
    ) {
        return comments.stream()
                .map(comment -> new CowalkCommentResponse(
                                comment.getId(),
                                memberMap.get(comment.getMemberId()).getProfileImageUrl(),
                                memberMap.get(comment.getMemberId()).getNickname(),
                                comment.getCreatedAt().toLocalDate(),
                                comment.getContent()
                        )
                )
                .toList();
    }
}