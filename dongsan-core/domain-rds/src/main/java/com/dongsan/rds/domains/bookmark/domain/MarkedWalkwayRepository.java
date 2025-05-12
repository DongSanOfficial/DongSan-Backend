package com.dongsan.rds.domains.bookmark.domain;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MarkedWalkwayRepository {

    Optional<LocalDateTime> getBookmarkedDate(Long bookmarkId, Long walkwayId);
    
    //Map<Long, Boolean> existsMarkedWalkway(Long walkwayId, List<Long> bookmarkIds);

    boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    boolean isWalkwayAdded(Long bookmarkId, Long walkwayId);

    void includeWalkway(Long bookmarkId, Long walkwayId);

    void excludeWalkway(Long bookmarkId, Long walkwayId);

    void deleteAllByBookmarkId(Long bookmarkId);
}
