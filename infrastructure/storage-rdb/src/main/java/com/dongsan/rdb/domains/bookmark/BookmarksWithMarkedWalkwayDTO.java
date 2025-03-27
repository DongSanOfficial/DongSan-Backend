package com.dongsan.rdb.domains.bookmark;

import java.time.LocalDateTime;

import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;

public record BookmarksWithMarkedWalkwayDTO(
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
