package com.dongsan.api.domains.bookmark;

import java.util.List;

import com.dongsan.core.domains.bookmark.Bookmark;

public record BookmarksNameResponse(
	Long bookmarkId,
	String title
) {
	public BookmarksNameResponse(Bookmark bookmark) {
		this(
			bookmark.bookmarkId(),
			bookmark.title()
		);
	}

	public static List<BookmarksNameResponse> from(List<Bookmark> bookmarks) {
		return bookmarks.stream()
			.map(BookmarksNameResponse::new)
			.toList();
	}
}

