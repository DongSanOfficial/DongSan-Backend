package com.dongsan.api.domains.member;

import com.dongsan.core.domains.member.Member;

public record MemberProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
    public MemberProfileResponse(Member member){
        this(
                member.profileImageUrl(),
                member.email(),
                member.nickname()
        );
    }
}
