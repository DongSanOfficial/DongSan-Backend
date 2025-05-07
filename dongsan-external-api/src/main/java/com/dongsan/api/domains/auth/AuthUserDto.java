package com.dongsan.api.domains.auth;

import com.dongsan.rds.domains.member.Member;
import com.dongsan.rds.domains.member.MemberRole;

public record AuthUserDto(
        Long memberId,
        String email,
        String nickname,
        String profileImageUrl,
        MemberRole role
) {
    public AuthUserDto(Member member) {
        this(member.getId(), member.getEmail(), member.getNickname(), member.getProfileImageUrl(), member.getRole());
    }

    public Member toMember() {
        return new Member(memberId, email, nickname, profileImageUrl, role);
    }
}
