package com.dongsan.core.domains.member;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Member save(String email, String nickname, String profileImageUrl, MemberRole role);
}
