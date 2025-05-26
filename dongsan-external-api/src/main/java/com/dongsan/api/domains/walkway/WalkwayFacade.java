package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayDetailResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayHistoryResponse;
import com.dongsan.file.service.S3FileService;
import com.dongsan.rdb.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.rdb.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.rdb.domains.image.ImageRdbService;
import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.review.service.ReviewRdbService;
import com.dongsan.rdb.domains.walkway.CreateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.domain.WalkwaySnapshot;
import com.dongsan.rdb.domains.walkway.factory.SearchWalkwayFactory;
import com.dongsan.rdb.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLog;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class WalkwayFacade {
    private final WalkwayRdbService walkwayRdbService;
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final BookmarkRdbService bookmarkRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final ReviewRdbService reviewRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;
    private final SearchWalkwayFactory searchWalkwayFactory;

    public WalkwayFacade(WalkwayRdbService walkwayRdbService, LikedWalkwayRdbService likedWalkwayRdbService,
                         BookmarkRdbService bookmarkRdbService, WalkwayLogRdbService walkwayLogRdbService,
                         ReviewRdbService reviewRdbService, S3FileService s3FileService,
                         ImageRdbService imageRdbService, SearchWalkwayFactory searchWalkwayFactory) {
        this.walkwayRdbService = walkwayRdbService;
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.bookmarkRdbService = bookmarkRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.reviewRdbService = reviewRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
        this.searchWalkwayFactory = searchWalkwayFactory;
    }

    public Long createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {
        String imageUrl = imageRdbService.getImage(createWalkwayRequest.courseImageId()).getUrl();
        CreateWalkwayCommand command = createWalkwayRequest.toCreateWalkwayCommand(imageUrl, memberId);
        return walkwayRdbService.save(command);
    }

    public void updateWalkway(UpdateWalkwayCommand command, Long memberId) {
        walkwayRdbService.update(command, memberId);
    }

    // 더 나은 방법이 뭐가 있을지 추가 고민 필요 (우선은 batch 다 삭제하는 걸로 구현)
    public void deleteWalkway(Long walkwayId, Long memberId) {
        walkwayRdbService.delete(walkwayId, memberId);
        reviewRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        walkwayLogRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        likedWalkwayRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        bookmarkRdbService.deleteAllMarkedWalkwayInBatchByWalkwayId(walkwayId);
    }

    public Long saveImage(MultipartFile courseImage) {
        String imageUrl = s3FileService.saveFile(courseImage);
        return imageRdbService.save(imageUrl);
    }

    public CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId,
                                                                              CursorRequest paging) {
        walkwayRdbService.getWalkway(walkwayId);
        return bookmarkRdbService.getBookmarksWithMarkedWalkway(walkwayId, memberId, paging.lastId(), paging.size());
    }

    @Transactional(readOnly = true)
    public WalkwayDetailResponse getWalkwayDetail(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayRdbService.getWalkwayWithAccessValidation(walkwayId, memberId);
        ReviewStatistic reviewStatistic = reviewRdbService.getReviewStat(walkwayId);
        int likeCount = likedWalkwayRdbService.countLikes(walkwayId);
        boolean isLike = likedWalkwayRdbService.isLiked(memberId, walkwayId);
        boolean isMarked = bookmarkRdbService.wasEverBookmarked(memberId, walkwayId);
        return new WalkwayDetailResponse(walkway.snapshot(), isLike, isMarked, reviewStatistic, likeCount);
    }

    public WalkwayHistoryResponse createHistoryLog(Long walkwayId, Long memberId, Double distance, Integer time) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        WalkwayLog walkwayLog = walkwayLogRdbService.save(walkwayId, memberId, distance, time);
        boolean canReview = walkwayLog.isSufficientDistance(walkway.getDistance());
        return new WalkwayHistoryResponse(walkwayId, canReview);
    }

    public CursorPage<SearchWalkwayResponse> searchWalkway(SearchWalkwayQuery searchQuery, CursorRequest paging) {
        CursorPage<Walkway> walkways = searchWalkwayFactory.getService(searchQuery.sort()).search(searchQuery, paging);
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();

        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        Set<Long> likedWalkwaySet = likedWalkwayRdbService.likedWalkways(searchQuery.memberId(), walkwayIds);

        List<SearchWalkwayResponse> response = SearchWalkwayResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap, likedWalkwaySet);
        return new CursorPage<>(response, walkways.getHasNext());
    }

    @Transactional(readOnly = true)
    public CursorPage<SearchWalkwayResponse> getWalkwaysLatest(Long memberId, String sortType, CursorRequest paging) {
        WalkwaySort sort = WalkwaySort.typeOf(sortType);
        CursorPage<Walkway> walkways = switch (sort) {
            case LIKED -> walkwayRdbService.getWalkwaysLiked(memberId, paging.lastId(), paging.size());
            case RATING -> walkwayRdbService.getWalkwaysRating(memberId, paging.lastId(), paging.size());
            case LATEST -> walkwayRdbService.getWalkwaysLatest(memberId, paging.lastId(), paging.size());
        };
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();

        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        Set<Long> likedWalkwaySet = likedWalkwayRdbService.likedWalkways(memberId, walkwayIds);

        List<SearchWalkwayResponse> response = SearchWalkwayResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap, likedWalkwaySet);
        return new CursorPage<>(response, walkways.getHasNext());
    }


    //    public PagingResponse<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
//                                                                     Long lastWalkwayHistoryId) {
//
//        LocalDateTime lastCreatedAt = null;
//        if (lastWalkwayHistoryId != null) {
//            WalkwayHistory walkwayHistory = getWalkwayHistory(lastWalkwayHistoryId);
//            lastCreatedAt = walkwayHistory.createdAt();
//        }
//        List<WalkwayHistory> walkwayHistories = getCanReviewWalkwayHistory(walkwayId, memberId, size + 1,
//                lastCreatedAt);
//        return PagingResponse.from(walkwayHistories, size);
//    }
//
//    public WalkwayHistory getWalkwayHistory(Long walkwayHistoryId) {
//        return walkwayRepository.getWalkwayHistory(walkwayHistoryId)
//                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_LOG_NOT_FOUND));
//    }
//
//    public List<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
//                                                           LocalDateTime lastCreatedAt) {
//        return walkwayRepository.getCanReviewWalkwayHistory(walkwayId, memberId, size, lastCreatedAt);
//    }


}
