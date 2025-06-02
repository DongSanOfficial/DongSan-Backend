package com.dongsan.domain.domains.bookmark.domain;

import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MarkedWalkwayRepository {

    Optional<LocalDateTime> getBookmarkedDate(Long bookmarkId, Long walkwayId);

    CursorResponse<MarkedWalkway> getBookmarkWalkway(Long memberId, Long bookmarkId, LocalDateTime lastCreatedAt, int size);

    boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    boolean isWalkwayAdded(Long bookmarkId, Long walkwayId);

    void includeWalkway(Long bookmarkId, Long walkwayId);

    void excludeWalkway(Long bookmarkId, Long walkwayId);

    void deleteAllByBookmarkId(Long bookmarkId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

}
