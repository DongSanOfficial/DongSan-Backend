package com.dongsan.core.domains.member;

import com.dongsan.core.domains.auth.Provider;
import org.springframework.stereotype.Component;

@Component
public class MemberWriter {
    private final MemberRepository memberRepository;

    public MemberWriter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        return memberRepository.save(email, nickname, profileImageUrl, role, provider);
    }
}
