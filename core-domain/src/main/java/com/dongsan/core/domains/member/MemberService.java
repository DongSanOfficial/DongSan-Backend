package com.dongsan.core.domains.member;

import com.dongsan.core.domains.auth.Provider;
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

    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider){
        return memberWriter.save(email, nickname, profileImageUrl, role, provider);
    }

    public Optional<Member> getOptionalMemberByEmailAndProvider(String email, Provider provider){
        return memberReader.readOptionalMemberByEmailAndProvider(email, provider);
    }
}
