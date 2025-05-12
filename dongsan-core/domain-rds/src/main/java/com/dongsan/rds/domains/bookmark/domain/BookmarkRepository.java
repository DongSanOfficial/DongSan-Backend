package com.dongsan.rds.domains.bookmark.domain;

import com.dongsan.rds.common.CursorPage;
import com.dongsan.rds.domains.bookmark.BookmarkWithMarkedStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookmarkRepository {
    Long save(Bookmark bookmark);

    Optional<Bookmark> findById(Long bookmarkId);

    CursorPage<Bookmark> getUserBookmarks(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedStatus(Long walkwayId, Long memberId,
                                                                      LocalDateTime createdAt,
                                                                      int size);

    boolean existsByMemberIdAndName(Long memberId, String name);

    void deleteById(Long bookmarkId);
}
