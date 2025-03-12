package com.dongsan.rdb.domains.member;

import com.dongsan.core.domains.auth.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByEmailAndProvider(String email, Provider provider);
}
