package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.core.domains.walkway.WalkwayReader;
import com.dongsan.core.domains.walkway.WalkwayValidator;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;

@Service
public class BookmarkService {
	private final BookmarkReader bookmarkReader;
	private final BookmarkWriter bookmarkWriter;
	private final BookmarkValidator bookmarkValidator;
	private final WalkwayReader walkwayReader;
	private final WalkwayValidator walkwayValidator;

	public BookmarkService(BookmarkReader bookmarkReader, BookmarkWriter bookmarkWriter,
		BookmarkValidator bookmarkValidator, WalkwayReader walkwayReader,
		WalkwayValidator walkwayValidator) {
		this.bookmarkReader = bookmarkReader;
		this.bookmarkWriter = bookmarkWriter;
		this.bookmarkValidator = bookmarkValidator;
		this.walkwayReader = walkwayReader;
		this.walkwayValidator = walkwayValidator;
	}

	@Transactional
	public Long createBookmark(Long memberId, String name) {
		bookmarkValidator.validateUniqueBookmarkName(memberId, name);
		return bookmarkWriter.createBookmark(memberId, name);
	}

	@Transactional
	public void renameBookmark(Long memberId, Long bookmarkId, String name) {
		Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
		bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
		bookmarkValidator.validateUniqueBookmarkName(memberId, name);
		bookmarkWriter.renameBookmark(bookmarkId, name);
	}

	@Transactional
	public void includeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
		Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
		walkwayValidator.validateWalkwayExists(walkwayId);
		bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
		bookmarkValidator.validateWalkwayNotInBookmark(bookmarkId, walkwayId);
		bookmarkWriter.includeWalkway(bookmarkId, walkwayId);
	}

	@Transactional
	public void excludeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
		Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
		walkwayValidator.validateWalkwayExists(walkwayId);
		bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
		bookmarkValidator.validateWalkwayExistsInBookmark(bookmarkId, walkwayId);
		bookmarkWriter.excludeWalkway(bookmarkId, walkwayId);
	}

	@Transactional
	public void deleteBookmark(Long memberId, Long bookmarkId) {
		Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
		bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
		bookmarkWriter.deleteBookmark(bookmarkId);
	}

	public PagingResponse<MarkedWalkway> getBookmarkWalkways(Long memberId, Long bookmarkId, CursorRequest paging) {
		Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
		bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
		// 마지막 markedBookmark의 생성시간 조회
		LocalDateTime lastCreatedAt = paging.lastId() == null ? null : bookmarkReader.getBookmarkedDate(bookmarkId,
			walkwayReader.getWalkway(paging.lastId())
				.walkwayId());
		List<MarkedWalkway> markedWalkways = bookmarkReader.getBookmarkWalkway(bookmarkId, paging.size() + 1,
			lastCreatedAt, memberId);
		return PagingResponse.from(markedWalkways, paging.size());
	}

	public PagingResponse<Bookmark> getUserBookmarksName(Long memberId, CursorRequest paging) {
		LocalDateTime createdAt = paging.lastId() == null ? null : bookmarkReader.getBookmark(paging.lastId())
			.createdAt();
		List<Bookmark> bookmarks = bookmarkReader.getUserBookmarkNames(createdAt, memberId, paging.size());
		return PagingResponse.from(bookmarks, paging.size());
	}

	public PagingResponse<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId,
		CursorRequest paging) {
		walkwayValidator.validateWalkwayExists(walkwayId);
		LocalDateTime createdAt = paging.lastId() == null ? null : bookmarkReader.getBookmark(paging.lastId())
			.createdAt();
		List<BookmarkWithMarkedStatus> bookmarks = bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId,
			createdAt,
			paging.size());
		return PagingResponse.from(bookmarks, paging.size());
	}

	public boolean existsMarkedWalkway(Long memberId, Long walkwayId) {
		return bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId);
	}

	public Map<Long, Boolean> existsMarkedWalkways(Long walkwayId, List<Long> bookmarkIds) {
		return bookmarkReader.existsMarkedWalkway(walkwayId, bookmarkIds);
	}

}
