package com.dongsan.api.domains.walkway.dto.response;

import java.util.List;

import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;

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
