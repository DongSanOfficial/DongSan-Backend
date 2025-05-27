package com.dongsan.domain.domains.bookmark.service;

import com.dongsan.domain.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.domain.domains.bookmark.domain.MarkedWalkwayRepository;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.domains.bookmark.domain.Bookmark;
import com.dongsan.domain.domains.bookmark.domain.BookmarkRepository;
import com.dongsan.domain.domains.bookmark.domain.MarkedWalkway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class BookmarkRdbService {
    private final BookmarkRepository bookmarkRepository;
    private final MarkedWalkwayRepository markedWalkwayRepository;

    public BookmarkRdbService(BookmarkRepository bookmarkRepository, MarkedWalkwayRepository markedWalkwayRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.markedWalkwayRepository = markedWalkwayRepository;
    }

    public Bookmark getBookmark(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(
                        () -> new CoreException(CoreErrorCode.BOOKMARK_NOT_EXIST)
                );
    }

    public LocalDateTime getBookmarkCreatedAt(Long bookmarkId) {
        if (bookmarkId == null) {
            return null;
        }
        return getBookmark(bookmarkId).getCreatedAt();
    }

    public LocalDateTime getBookmarkedDate(Long bookmarkId, Long walkwayId) {
        if (walkwayId == null) {
            return null;
        }
        return markedWalkwayRepository.getBookmarkedDate(bookmarkId, walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK));
    }

    public Long save(Long memberId, String name) {
        validateUniqueName(memberId, name);
        Bookmark newBookmark = new Bookmark(name, memberId);
        return bookmarkRepository.save(newBookmark);
    }

    public void rename(Long memberId, Long bookmarkId, String name) {
        Bookmark bookmark = getBookmark(bookmarkId);
        validateUniqueName(memberId, name);
        bookmark.rename(name, memberId);
    }

    public void includeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Bookmark bookmark = getBookmark(bookmarkId);
        bookmark.validateOwner(memberId);
        validateWalkwayNotInBookmark(bookmarkId, walkwayId);
        markedWalkwayRepository.includeWalkway(bookmarkId, walkwayId);
    }

    public void excludeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        Bookmark bookmark = getBookmark(bookmarkId);
        bookmark.validateOwner(memberId);
        validateWalkwayExistsInBookmark(bookmarkId, walkwayId);
        markedWalkwayRepository.excludeWalkway(bookmarkId, walkwayId);
    }

    public void delete(Long memberId, Long bookmarkId) {
        Bookmark bookmark = getBookmark(bookmarkId);
        bookmark.validateOwner(memberId);
        bookmarkRepository.deleteById(bookmarkId);
        markedWalkwayRepository.deleteAllByBookmarkId(bookmarkId);
    }

    public boolean wasEverBookmarked(Long memberId, Long walkwayId) {
        return markedWalkwayRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    public CursorPage<Bookmark> getUserBookmark(Long memberId, Long lastBookmarkId, int size) {
        LocalDateTime lastCreatedAt = getBookmarkCreatedAt(lastBookmarkId);
        return bookmarkRepository.getUserBookmarks(memberId, lastCreatedAt, size);
    }

    public CursorPage<MarkedWalkway> getBookmarkWalkway(Long memberId, Long bookmarkId, Long lastWalkwayId, int size) {
        LocalDateTime lastCreatedAt = getBookmarkedDate(bookmarkId, lastWalkwayId);
        return markedWalkwayRepository.getBookmarkWalkway(memberId, bookmarkId, lastCreatedAt, size);
    }

    public CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId,
                                                                              Long lastBookmarkId, int size) {
        LocalDateTime lastCreatedAt = getBookmarkCreatedAt(lastBookmarkId);
        return bookmarkRepository.getBookmarksWithMarkedStatus(walkwayId, memberId, lastCreatedAt, size);
    }

    private void validateUniqueName(Long memberId, String name) {
        if (bookmarkRepository.existsByMemberIdAndName(memberId, name)) {
            throw new CoreException(CoreErrorCode.SAME_BOOKMARK_NAME_EXIST);
        }
    }

    private void validateWalkwayNotInBookmark(Long bookmarkId, Long walkwayId) {
        if (markedWalkwayRepository.isWalkwayAdded(bookmarkId, walkwayId)) {
            throw new CoreException(CoreErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK);
        }
    }

    private void validateWalkwayExistsInBookmark(Long bookmarkId, Long walkwayId) {
        if (!markedWalkwayRepository.isWalkwayAdded(bookmarkId, walkwayId)) {
            throw new CoreException(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK);
        }
    }

    public void deleteAllMarkedWalkwayInBatchByWalkwayId(Long walkwayId) {
        markedWalkwayRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

}
