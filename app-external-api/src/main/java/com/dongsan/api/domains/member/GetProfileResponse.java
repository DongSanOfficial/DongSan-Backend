package com.dongsan.api.domains.member;

import com.dongsan.core.domains.member.Member;

public record GetProfileResponse(
        String profileImageUrl,
        String email,
        String nickname
) {
    public GetProfileResponse(Member member){
        this(
                member.profileImageUrl(),
                member.email(),
                member.nickname()
        );
    }
}
