package com.dongsan.core.domains.member;

import org.springframework.stereotype.Component;

@Component
public class MemberWriter {
    private final MemberRepository memberRepository;

    public MemberWriter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(String email, String nickname, String profileImageUrl, MemberRole role) {
        return memberRepository.save(email, nickname, profileImageUrl, role);
    }
}
