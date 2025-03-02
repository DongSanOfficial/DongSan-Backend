package com.dongsan.rdb.domains.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    boolean existsByMemberIdAndName(Long memberId, String name);
}
