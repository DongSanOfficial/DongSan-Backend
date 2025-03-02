package com.dongsan.rdb.domains.bookmark;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkedWalkwayJpaRepository extends JpaRepository<MarkedWalkwayEntity, Long> {
    boolean existsByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);
    void deleteByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from MarkedWalkwayEntity mw where mw.bookmark.id = :bookmarkId")
    void deleteAllByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    int countByBookmarkId(Long bookmarkId);
    Optional<MarkedWalkwayEntity> findByBookmarkIdAndWalkwayId(Long bookmarkId, Long walkwayId);

}
