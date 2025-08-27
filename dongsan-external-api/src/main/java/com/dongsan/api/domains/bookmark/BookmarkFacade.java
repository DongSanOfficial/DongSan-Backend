package com.dongsan.api.domains.bookmark;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.bookmark.domain.Bookmark;
import com.dongsan.domain.domains.bookmark.domain.MarkedWalkway;
import com.dongsan.domain.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.service.ReviewRdbService;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.domain.domains.walkway.service.WalkwayRdbService;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;

@Component
@Transactional
public class BookmarkFacade {
    private final BookmarkRdbService bookmarkRdbService;
    private final WalkwayRdbService walkwayRdbService;
    private final ReviewRdbService reviewRdbService;
    private final LikedWalkwayRdbService likedWalkwayRdbService;


    public BookmarkFacade(BookmarkRdbService bookmarkRdbService, WalkwayRdbService walkwayRdbService, ReviewRdbService reviewRdbService, LikedWalkwayRdbService likedWalkwayRdbService) {
        this.bookmarkRdbService = bookmarkRdbService;
        this.walkwayRdbService = walkwayRdbService;
        this.reviewRdbService = reviewRdbService;
        this.likedWalkwayRdbService = likedWalkwayRdbService;
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

    public CursorResponse<Bookmark> getUserBookmark(Long memberId, CursorRequest paging) {
        return bookmarkRdbService.getUserBookmark(memberId, paging.lastId(), paging.size());
    }

    public CursorResponse<MarkedWalkwayResponse> getBookmarkWalkways(Long memberId, Long bookmarkId, CursorRequest paging) {
        Bookmark bookmark = bookmarkRdbService.getBookmark(bookmarkId);
        bookmark.validateOwner(memberId);
        CursorResponse<MarkedWalkway> markedWalkways = bookmarkRdbService.getBookmarkWalkway(memberId, bookmarkId, paging.lastId(), paging.size());

        List<Long> walkwayIds = markedWalkways.data().stream().map(MarkedWalkway::getWalkwayId).toList();
        Map<Long, Walkway> walkwayMap = walkwayRdbService.getWalkways(walkwayIds);
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);

        List<MarkedWalkwayResponse> response = MarkedWalkwayResponse.from(markedWalkways.data(), walkwayMap, reviewStatMap, likeCountMap);
        return new CursorResponse<>(response, markedWalkways.hasNext());
    }

}
