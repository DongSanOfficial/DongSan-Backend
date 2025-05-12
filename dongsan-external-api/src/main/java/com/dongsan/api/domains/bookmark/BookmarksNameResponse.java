package com.dongsan.api.domains.bookmark;

import com.dongsan.rdb.domains.bookmark.domain.Bookmark;

import java.util.List;

public record BookmarksNameResponse(
        Long bookmarkId,
        String title
) {
    public BookmarksNameResponse(Bookmark bookmark) {
        this(
                bookmark.getId(),
                bookmark.getName()
        );
    }

    public static List<BookmarksNameResponse> from(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(BookmarksNameResponse::new)
                .toList();
    }
}

