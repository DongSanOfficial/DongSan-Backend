package com.dongsan.rdb.domains.member;

import com.dongsan.rdb.domains.auth.Provider;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider);

    Optional<Member> findByEmailAndProvider(String email, Provider provider);

}
