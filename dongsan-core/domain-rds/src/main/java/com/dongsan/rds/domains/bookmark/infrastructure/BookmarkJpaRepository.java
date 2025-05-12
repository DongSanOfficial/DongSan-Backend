package com.dongsan.rds.domains.bookmark.infrastructure;

import com.dongsan.rds.domains.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberIdAndName(Long memberId, String name);
}
