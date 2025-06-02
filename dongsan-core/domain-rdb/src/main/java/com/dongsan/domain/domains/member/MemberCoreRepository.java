package com.dongsan.domain.domains.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.auth.Provider;

@Repository
public class MemberCoreRepository implements MemberRepository {
	private final MemberJpaRepository memberJpaRepository;

	public MemberCoreRepository(MemberJpaRepository memberJpaRepository) {
		this.memberJpaRepository = memberJpaRepository;
	}

	@Override
	public Optional<Member> findById(Long memberId) {
		return memberJpaRepository.findById(memberId);
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email);
	}

	@Override
	public Member save(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
		Member member = new Member(email, nickname, profileImageUrl, role, provider);
		memberJpaRepository.save(member);
		return member;
	}

	@Override
	public Optional<Member> findByEmailAndProvider(String email, Provider provider) {
		return memberJpaRepository.findByEmailAndProvider(email, provider);
	}

	@Override
	public List<Member> findAllByIdIn(List<Long> ids) {
		return memberJpaRepository.findAllByIdIn(ids);
	}
}
