package com.dongsan.domain.domains.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.auth.Provider;

@Repository
public interface MemberRepository {
	Optional<Member> findById(Long memberId);

	Optional<Member> findByEmail(String email);

	Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider);

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

	List<Member> findAllByIdIn(List<Long> ids);
}
