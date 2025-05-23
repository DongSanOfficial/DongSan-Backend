package com.dongsan.api.domains.bookmark;

import com.dongsan.rdb.domains.bookmark.domain.Bookmark;
import com.dongsan.rdb.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import com.dongsan.rdb.query.MarkedWalkwayParam;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
public class BookmarkFacade {
    private final BookmarkRdbService bookmarkRdbService;
    private final WalkwayRdbService walkwayRdbService;

    public BookmarkFacade(BookmarkRdbService bookmarkRdbService, WalkwayRdbService walkwayRdbService) {
        this.bookmarkRdbService = bookmarkRdbService;
        this.walkwayRdbService = walkwayRdbService;
    }

    public Long save(Long memberId, String name) {
        return bookmarkRdbService.save(memberId, name);
    }

    public void rename(Long memberId, Long bookmarkId, String name) {
        bookmarkRdbService.rename(memberId, bookmarkId, name);
    }

    public void includeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        walkwayRdbService.getWalkway(walkwayId);
        bookmarkRdbService.includeWalkway(memberId, bookmarkId, walkwayId);
    }

    public void excludeWalkway(Long memberId, Long bookmarkId, Long walkwayId) {
        walkwayRdbService.getWalkway(walkwayId);
        bookmarkRdbService.excludeWalkway(memberId, bookmarkId, walkwayId);
    }

    public void delete(Long memberId, Long walkwayId) {
        bookmarkRdbService.delete(memberId, walkwayId);
    }

    public CursorPage<Bookmark> getUserBookmark(Long memberId, CursorRequest paging) {
        // TODO : lastCreated 로직도 서비스 안에 넣기
        LocalDateTime lastCreatedAt = bookmarkRdbService.getBookmarkCreatedAt(paging.lastId());
        return bookmarkRdbService.getUserBookmark(memberId, lastCreatedAt, paging.size());
    }

    public CursorPage<MarkedWalkwayResponse> getBookmarkWalkways(Long memberId, Long bookmarkId, CursorRequest paging) {
        Bookmark bookmark = bookmarkRdbService.getBookmark(bookmarkId);
        bookmark.validateOwner(memberId);

        LocalDateTime lastCreatedAt = bookmarkRdbService.getBookmarkedDate(bookmarkId, paging.lastId());
        CursorPage<MarkedWalkwayParam> result = bookmarkRdbService.getBookmarkWalkway(memberId, bookmarkId, lastCreatedAt, paging.size() + 1);
        // TODO : 따로 매핑 필요
        return null;
    }

}
