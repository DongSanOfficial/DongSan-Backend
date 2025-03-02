package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.List;
import java.util.Map;

public record BookmarksWithMarkedWalkwayResponse(
        List<BookmarkWithMarkedWalkway> bookmarks,
        Boolean hasNext
) {
    public BookmarksWithMarkedWalkwayResponse(CursorPagingResponse<BookmarkWithMarkedStatus> response) {
        this(
                response.data().stream()
                        .map(bookmark -> new BookmarkWithMarkedWalkway(bookmark.bookmarkId(), bookmark.title(), bookmark.marked()))
                        .toList(),
                response.hasNext()
        );
    }

    public record BookmarkWithMarkedWalkway (
            Long bookmarkId,
            String name,
            Boolean marked
    ) {}
}
