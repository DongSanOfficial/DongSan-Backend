package com.dongsan.rdb.domains.bookmark.service;

import com.dongsan.rdb.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.rdb.domains.bookmark.domain.Bookmark;
import com.dongsan.rdb.domains.bookmark.domain.BookmarkRepository;
import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkwayRepository;
import com.dongsan.rdb.query.MarkedWalkwayParam;
import com.dongsan.rdb.query.MarkedWalkwayQueryRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class BookmarkRdbService {
    private final BookmarkRepository bookmarkRepository;
    private final MarkedWalkwayRepository markedWalkwayRepository;
    private final MarkedWalkwayQueryRepository markedWalkwayQueryRepository;

    public BookmarkRdbService(BookmarkRepository bookmarkRepository, MarkedWalkwayRepository markedWalkwayRepository, MarkedWalkwayQueryRepository markedWalkwayQueryRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.markedWalkwayRepository = markedWalkwayRepository;
        this.markedWalkwayQueryRepository = markedWalkwayQueryRepository;
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

    public CursorPage<Bookmark> getUserBookmark(Long memberId, LocalDateTime lastCreatedAt, int size) {
        return bookmarkRepository.getUserBookmarks(memberId, lastCreatedAt, size);
    }

    public CursorPage<MarkedWalkwayParam> getBookmarkWalkway(Long memberId, Long bookmarkId, LocalDateTime lastCreatedAt, int size) {
        return markedWalkwayQueryRepository.getBookmarkWalkway(memberId, bookmarkId, lastCreatedAt, size);
    }

    public CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId,
                                                                              Long lastId,
                                                                              int size) {
        LocalDateTime createdAt = getBookmarkCreatedAt(lastId);
        return bookmarkRepository.getBookmarksWithMarkedStatus(walkwayId, memberId, createdAt, size);
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

//    public Map<Long, Boolean> existsMarkedWalkways(Long walkwayId, List<Long> bookmarkIds) {
//        return markedWalkwayRepository.existsMarkedWalkway(walkwayId, bookmarkIds);
//    }

}
