package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;

import com.dongsan.core.support.util.Author;

public record Bookmark(
	Long bookmarkId,
	String title,
	Author author,
	LocalDateTime createdAt
) {
}
