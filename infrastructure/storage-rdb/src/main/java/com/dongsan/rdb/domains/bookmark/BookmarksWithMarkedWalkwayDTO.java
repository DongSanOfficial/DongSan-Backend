package com.dongsan.rdb.domains.bookmark;

import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import java.time.LocalDateTime;

public record BookmarksWithMarkedWalkwayDTO(
        Long bookmarkId,
        String name,
        LocalDateTime createdAt,
        Long markedWalkwayId
) {
    public BookmarkWithMarkedStatus toBookmarkWithMarkedStatus(){
        boolean marked = markedWalkwayId != null;
        return new BookmarkWithMarkedStatus(bookmarkId, name, createdAt, marked);
    }
}
