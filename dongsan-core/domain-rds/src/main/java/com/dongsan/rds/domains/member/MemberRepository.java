package com.dongsan.rds.domains.member;

import com.dongsan.core.domains.auth.Provider;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider);

    Optional<Member> findByEmailAndProvider(String email, Provider provider);

    void patchNickname(Long memberId, String nickname);
}
