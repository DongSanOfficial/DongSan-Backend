package com.dongsan.domain.domains.bookmark;

import java.time.LocalDateTime;

public record BookmarkWithMarkedWalkwayParam(
        Long bookmarkId,
        String name,
        LocalDateTime createdAt,
        Long markedWalkwayId
) {
    public BookmarkWithMarkedStatus toBookmarkWithMarkedStatus() {
        boolean marked = markedWalkwayId != null;
        return new BookmarkWithMarkedStatus(bookmarkId, name, createdAt, marked);
    }
}
