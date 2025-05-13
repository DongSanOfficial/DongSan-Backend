package com.dongsan.rdb.domains.member;

import com.dongsan.core.domains.auth.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndProvider(String email, Provider provider);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member set nickname = :nickname where id = :id")
    void updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
}
