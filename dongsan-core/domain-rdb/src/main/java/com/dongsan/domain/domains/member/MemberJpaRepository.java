package com.dongsan.domain.domains.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.auth.Provider;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

	List<Member> findAllByIdIn(List<Long> ids);
}
