package com.dongsan.core.domains.member;

public record Member(
        Long id,
        String email,
        String nickname,
        String profileImageUrl,
        MemberRole role
) {
}
