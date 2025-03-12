package com.dongsan.rdb.domains.member;

import com.dongsan.core.domains.auth.Provider;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRepository;
import com.dongsan.core.domains.member.MemberRole;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCoreRepository implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    public MemberCoreRepository(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .map(MemberEntity::toMember);
    }

    @Override
    public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        MemberEntity memberEntity = new MemberEntity(email, nickname, profileImageUrl, role, provider);
        System.out.println(role + " , "+ provider);
        System.out.println(memberEntity.getProvider());
        memberJpaRepository.save(memberEntity);
        return memberEntity.toMember();
    }

    @Override
    public Optional<Member> findByEmailAndProvider(String email, Provider provider) {
        return memberJpaRepository.findByEmailAndProvider(email, provider)
                .map(MemberEntity::toMember);
    }
}
