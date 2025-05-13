package com.dongsan.rdb.domains.bookmark.infrastructure;

import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkedWalkwayJpaRepository extends JpaRepository<MarkedWalkway, Long> {
    boolean existsByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);

    void deleteByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from MarkedWalkway mw where mw.bookmarkId = :bookmarkId")
    void deleteAllByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    int countByBookmarkId(Long bookmarkId);

    Optional<MarkedWalkway> findByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
