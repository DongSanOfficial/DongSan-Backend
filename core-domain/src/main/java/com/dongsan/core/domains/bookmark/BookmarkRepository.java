package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository {
    Optional<Bookmark> findById(Long bookmarkId);

    Long save(Long memberId, String name);

    boolean existsById(Long bookmarkId);

    boolean existsByMemberIdAndName(Long memberId, String name);

    void rename(Long bookmarkId, String name);

    boolean isWalkwayAdded(Long bookmarkId, Long walkwayId);

    void includeWalkway(Long bookmarkId, Long walkwayId);

    void excludeWalkway(Long bookmarkId, Long walkwayId);

    void deleteById(Long bookmarkId);

    Optional<LocalDateTime> getBookmarkedDate(Long bookmarkId, Long walkwayId);

    List<MarkedWalkway> getBookmarkWalkways(Long bookmarkId, int size, LocalDateTime lastCreatedAt, Long memberId);

    List<Bookmark> getUserBookmarks(Integer size, LocalDateTime lastCreatedAt, Long memberId);

    List<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId, LocalDateTime createdAt, Integer size);

    Map<Long, Boolean> existsMarkedWalkway(Long walkwayId, List<Long> bookmarkIds);

    boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);
}
