package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.rdb.domains.bookmark.BookmarkWithMarkedStatus;

import java.util.List;

public record BookmarksWithMarkedWalkwayResponse(
        Long bookmarkId,
        String name,
        Boolean marked
) {
    public static List<BookmarksWithMarkedWalkwayResponse> from(List<BookmarkWithMarkedStatus> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> new BookmarksWithMarkedWalkwayResponse(bookmark.bookmarkId(), bookmark.title(),
                        bookmark.marked()))
                .toList();
    }

}
