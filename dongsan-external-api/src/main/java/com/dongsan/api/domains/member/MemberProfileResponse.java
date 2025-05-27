package com.dongsan.api.domains.member;

import com.dongsan.domain.domains.member.Member;

public record MemberProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
    public MemberProfileResponse(Member member) {
        this(
                member.getProfileImageUrl(),
                member.getEmail(),
                member.getNickname()
        );
    }
}
