package com.dongsan.api.domains.bookmark;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.List;

public record GetBookmarksNameResponse(
        List<BookmarkInfo> bookmarks,
        Boolean hasNext
) {
    public GetBookmarksNameResponse(CursorPagingResponse<Bookmark> response){
        this(
                response.data().stream().map(BookmarkInfo::new).toList(),
                response.hasNext()
        );
    }

    public record BookmarkInfo(
            Long bookmarkId,
            String title
    ) {
        public BookmarkInfo(Bookmark bookmark){
            this(
                    bookmark.bookmarkId(),
                    bookmark.title()
            );
        }
    }
}
