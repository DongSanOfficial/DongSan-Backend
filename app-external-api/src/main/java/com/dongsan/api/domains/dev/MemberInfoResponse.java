package com.dongsan.api.domains.dev;

import com.dongsan.core.domains.member.Member;

public record MemberInfoResponse(
        Long memberId,
        String email
) {
    public MemberInfoResponse(Member member){
        this(
                member.id(),
                member.email()
        );
    }
}
