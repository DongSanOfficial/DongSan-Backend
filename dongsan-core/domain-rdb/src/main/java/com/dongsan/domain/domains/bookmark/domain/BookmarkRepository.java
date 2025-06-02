package com.dongsan.domain.domains.bookmark.domain;

import com.dongsan.domain.domains.bookmark.infrastructure.dto.BookmarkWithMarkedStatus;
import com.dongsan.domain.support.util.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookmarkRepository {
    Long save(Bookmark bookmark);

    Optional<Bookmark> findById(Long bookmarkId);

    CursorResponse<Bookmark> getUserBookmarks(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorResponse<BookmarkWithMarkedStatus> getBookmarksWithMarkedStatus(Long walkwayId, Long memberId,
                                                                          LocalDateTime createdAt, int size);

    boolean existsByMemberIdAndName(Long memberId, String name);

    void deleteById(Long bookmarkId);
}
