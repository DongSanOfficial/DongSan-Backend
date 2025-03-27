package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;

@Component
public class BookmarkReader {
	private final BookmarkRepository bookmarkRepository;

	public BookmarkReader(BookmarkRepository bookmarkRepository) {
		this.bookmarkRepository = bookmarkRepository;
	}

	public Bookmark getBookmark(Long bookmarkId) {
		return bookmarkRepository.findById(bookmarkId)
			.orElseThrow(
				() -> new CoreException(CoreErrorCode.BOOKMARK_NOT_EXIST)
			);
	}

	public boolean existsById(Long bookmarkId) {
		return bookmarkRepository.existsById(bookmarkId);
	}

	public LocalDateTime getBookmarkedDate(Long bookmarkId, Long walkwayId) {
		return bookmarkRepository.getBookmarkedDate(bookmarkId, walkwayId)
			.orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK));
	}

	public List<MarkedWalkway> getBookmarkWalkway(Long bookmarkId, int size, LocalDateTime lastCreatedAt,
		Long memberId) {
		return bookmarkRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt, memberId);
	}

	public List<Bookmark> getUserBookmarkNames(LocalDateTime lastCreatedAt, Long memberId, Integer size) {
		return bookmarkRepository.getUserBookmarks(size, lastCreatedAt, memberId);
	}

	public List<BookmarkWithMarkedStatus> getBookmarksWithMarkedStatus(Long walkwayId, Long memberId,
		LocalDateTime createdAt, Integer size) {
		return bookmarkRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, createdAt, size);
	}

	public Map<Long, Boolean> existsMarkedWalkway(Long walkwayId, List<Long> bookmarkIds) {
		return bookmarkRepository.existsMarkedWalkway(walkwayId, bookmarkIds);
	}

	public boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId) {
		return bookmarkRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);
	}
}
