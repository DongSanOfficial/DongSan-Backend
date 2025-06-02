package com.dongsan.domain.domains.member;

import com.dongsan.domain.domains.auth.Provider;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MemberRdbService {
    private final MemberRepository memberRepository;

    public MemberRdbService(MemberRepository memberRepository) {
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

    @Transactional
    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        return memberRepository.save(email, nickname, profileImageUrl, role, provider);
    }

    @Transactional
    public void patchNickname(Long memberId, String nickname) {
        Member member = getMember(memberId);
        member.changeNickname(nickname);
    }

    public Map<Long, Member> getMemberMap(List<Long> memberIds) {
        List<Member> memberList = memberRepository.findAllByIdIn(memberIds);
        return memberList.stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
    }
}
