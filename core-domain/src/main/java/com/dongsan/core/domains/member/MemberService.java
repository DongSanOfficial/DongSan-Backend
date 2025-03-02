package com.dongsan.core.domains.member;

import java.util.Optional;
import org.springframework.stereotype.Service;

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

    public Optional<Member> getOptionalMemberByEmail(String email){
        return memberReader.readOptionalMemberByEmail(email);
    }

    public Member save(String email, String nickname, String profileImageUrl, MemberRole role){
        return memberWriter.save(email, nickname, profileImageUrl, role);
    }
}
