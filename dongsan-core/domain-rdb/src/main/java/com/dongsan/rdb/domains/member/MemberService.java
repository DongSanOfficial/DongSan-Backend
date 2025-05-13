package com.dongsan.rdb.domains.member;

import com.dongsan.core.domains.auth.Provider;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new CoreException(CoreErrorCode.MEMBER_NOT_FOUND)
                );
    }

    public Optional<Member> getOptionalMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> getOptionalMemberByEmailAndProvider(String email, Provider provider) {
        return memberRepository.findByEmailAndProvider(email, provider);
    }

    public Optional<Member> getOptionalMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        return memberRepository.save(email, nickname, profileImageUrl, role, provider);
    }

    @Transactional
    public void patchNickname(Long memberId, String nickname) {
        Member member = getMember(memberId);
        member.changeNickname(nickname);
    }
}
