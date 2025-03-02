package com.dongsan.api.domains.dev;

import com.dongsan.core.domains.member.Member;

public record GetMemberInfoResponse(
        Long memberId,
        String email
) {
    public GetMemberInfoResponse(Member member){
        this(
                member.id(),
                member.email()
        );
    }
}
