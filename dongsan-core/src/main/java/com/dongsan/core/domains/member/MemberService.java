package com.dongsan.core.domains.member;

import com.dongsan.core.domains.auth.Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberReader memberReader;
    private final MemberWriter memberWriter;

    public MemberService(MemberReader memberReader, MemberWriter memberWriter) {
        this.memberReader = memberReader;
        this.memberWriter = memberWriter;
    }

    public Member getMember(Long memberId) {
        return memberReader.readMember(memberId);
    }

    public Optional<Member> getOptionalMemberByEmail(String email) {
        return memberReader.readOptionalMemberByEmail(email);
    }

    @Transactional
    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        return memberWriter.save(email, nickname, profileImageUrl, role, provider);
    }

    public Optional<Member> getOptionalMemberByEmailAndProvider(String email, Provider provider) {
        return memberReader.readOptionalMemberByEmailAndProvider(email, provider);
    }

    @Transactional
    public void patchNickname(Long memberId, String nickname) {
        memberWriter.patchNickname(memberId, nickname);
    }
}
