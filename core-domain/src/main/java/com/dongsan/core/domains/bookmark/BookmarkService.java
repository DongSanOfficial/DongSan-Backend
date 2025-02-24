package com.dongsan.core.domains.bookmark;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import com.dongsan.core.support.util.CursorPagingRequest;
import com.dongsan.core.support.util.CursorPagingResponse;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {
    private final BookmarkReader bookmarkReader;
    private final BookmarkWriter bookmarkWriter;
    private final BookmarkValidator bookmarkValidator;
    private final WalkwayReader walkwayReader;

    public BookmarkService(BookmarkReader bookmarkReader, BookmarkWriter bookmarkWriter,
                           BookmarkValidator bookmarkValidator, WalkwayReader walkwayReader) {
        this.bookmarkReader = bookmarkReader;
        this.bookmarkWriter = bookmarkWriter;
        this.bookmarkValidator = bookmarkValidator;
        this.walkwayReader = walkwayReader;
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
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        bookmarkValidator.validateWalkwayNotInBookmark(bookmarkId, walkwayId);
        bookmarkWriter.includeWalkway(bookmarkId, walkwayId);
    }

    @Transactional
    public void excludeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
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

    public CursorPagingResponse<MarkedWalkway> getBookmarkWalkways(Long memberId, Long bookmarkId, CursorPagingRequest paging) {
        Bookmark bookmark = bookmarkReader.getBookmark(bookmarkId);
        bookmarkValidator.validateBookmarkOwner(memberId, bookmark);
        // 마지막 markedBookmark의 생성시간 조회
        LocalDateTime lastCreatedAt = paging.lastId() == null ? null : bookmarkReader.getBookmarkedDate(bookmarkId, walkwayReader.getWalkway(paging.lastId()).walkwayId());
        List<MarkedWalkway> markedWalkways = bookmarkReader.getBookmarkWalkway(bookmarkId, paging.size()+1, lastCreatedAt, memberId);
        return CursorPagingResponse.from(markedWalkways, paging.size());
    }

    public CursorPagingResponse<Bookmark> getUserBookmarksName(Long memberId, CursorPagingRequest paging) {
        LocalDateTime createdAt = paging.lastId() == null ? null : bookmarkReader.getBookmark(paging.lastId()).createdAt();
        List<Bookmark> bookmarks = bookmarkReader.getUserBookmarkNames(createdAt, memberId, paging.size());
        return CursorPagingResponse.from(bookmarks, paging.size());
    }

    public CursorPagingResponse<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId, CursorPagingRequest paging) {
        if (!walkwayReader.existsWalkway(walkwayId)) {
            throw new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND);
        }
        LocalDateTime createdAt = paging.lastId() == null ? null : bookmarkReader.getBookmark(paging.lastId()).createdAt();
        List<BookmarkWithMarkedStatus> bookmarks = bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId, createdAt,
                paging.size());
        return CursorPagingResponse.from(bookmarks, paging.size());
    }

    public boolean existsMarkedWalkway(Long memberId, Long walkwayId) {
        return bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    public Map<Long, Boolean> existsMarkedWalkways(Long walkwayId, List<Long> bookmarkIds) {
        return bookmarkReader.existsMarkedWalkway(walkwayId, bookmarkIds);
    }

}
