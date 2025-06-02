package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayDetailResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayHistoryResponse;
import com.dongsan.domain.domains.bookmark.infrastructure.dto.BookmarkWithMarkedStatus;
import com.dongsan.domain.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.domain.domains.image.ImageRdbService;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.service.ReviewRdbService;
import com.dongsan.domain.domains.walkway.CreateWalkwayCommand;
import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.domain.domains.walkway.WalkwaySort;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwaySnapshot;
import com.dongsan.domain.domains.walkway.factory.SearchWalkwayFactory;
import com.dongsan.domain.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.domain.domains.walkway.service.MetaWalkwayLikedRdbService;
import com.dongsan.domain.domains.walkway.service.MetaWalkwayRatingRdbService;
import com.dongsan.domain.domains.walkway.service.WalkwayRdbService;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.domain.support.util.CursorResponse;
import com.dongsan.file.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WalkwayFacade {
    private final WalkwayRdbService walkwayRdbService;
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final BookmarkRdbService bookmarkRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final ReviewRdbService reviewRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;
    private final SearchWalkwayFactory searchWalkwayFactory;
    private final MetaWalkwayLikedRdbService metaWalkwayLikedRdbService;
    private final MetaWalkwayRatingRdbService metaWalkwayRatingRdbService;

    public WalkwayFacade(WalkwayRdbService walkwayRdbService, LikedWalkwayRdbService likedWalkwayRdbService,
                         BookmarkRdbService bookmarkRdbService, WalkwayLogRdbService walkwayLogRdbService,
                         ReviewRdbService reviewRdbService, S3FileService s3FileService,
                         ImageRdbService imageRdbService, SearchWalkwayFactory searchWalkwayFactory, MetaWalkwayLikedRdbService metaWalkwayLikedRdbService, MetaWalkwayRatingRdbService metaWalkwayRatingRdbService) {
        this.walkwayRdbService = walkwayRdbService;
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.bookmarkRdbService = bookmarkRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.reviewRdbService = reviewRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
        this.searchWalkwayFactory = searchWalkwayFactory;
        this.metaWalkwayLikedRdbService = metaWalkwayLikedRdbService;
        this.metaWalkwayRatingRdbService = metaWalkwayRatingRdbService;
    }

    @Transactional
    public Long createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {
        String imageUrl = imageRdbService.getImage(createWalkwayRequest.courseImageId()).getUrl();
        CreateWalkwayCommand command = createWalkwayRequest.toCreateWalkwayCommand(imageUrl, memberId);
        Long walkwayId = walkwayRdbService.save(command);
        metaWalkwayLikedRdbService.saveByWalkwayId(walkwayId);
        metaWalkwayRatingRdbService.saveByWalkwayId(walkwayId);
        return walkwayId;
    }

    @Transactional
    public void updateWalkway(UpdateWalkwayCommand command, Long memberId) {
        walkwayRdbService.update(command, memberId);
    }

    // 더 나은 방법이 뭐가 있을지 추가 고민 필요 (우선은 batch 다 삭제하는 걸로 구현)
    @Transactional
    public void deleteWalkway(Long walkwayId, Long memberId) {
        walkwayRdbService.delete(walkwayId, memberId);
        reviewRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        walkwayLogRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        likedWalkwayRdbService.deleteAllInBatchByWalkwayId(walkwayId);
        bookmarkRdbService.deleteAllMarkedWalkwayInBatchByWalkwayId(walkwayId);
    }

    @Transactional
    public Long saveImage(MultipartFile image) {
        String imageUrl = s3FileService.saveFile(image);
        return imageRdbService.save(imageUrl);
    }

    @Transactional(readOnly = true)
    public CursorResponse<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId,
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

    @Transactional
    public WalkwayHistoryResponse createHistoryLog(Long walkwayId, Long memberId, Double distance, Integer time) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        WalkwayLog walkwayLog = walkwayLogRdbService.save(walkwayId, memberId, distance, time);
        boolean canReview = walkwayLog.isSufficientDistance(walkway.getDistance());
        return new WalkwayHistoryResponse(walkwayId, canReview);
    }

    @Transactional(readOnly = true)
    public CursorResponse<SearchWalkwayResponse> searchWalkway(SearchWalkwayQuery searchQuery, CursorRequest paging) {
        CursorResponse<Walkway> walkways = searchWalkwayFactory.getService(searchQuery.sort()).search(searchQuery, paging);
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();

        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        Set<Long> likedWalkwaySet = likedWalkwayRdbService.likedWalkways(searchQuery.memberId(), walkwayIds);

        List<SearchWalkwayResponse> response = SearchWalkwayResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap, likedWalkwaySet);
        return new CursorResponse<>(response, walkways.getHasNext());
    }

    @Transactional(readOnly = true)
    public CursorResponse<SearchWalkwayResponse> getWalkwaysLatest(Long memberId, String sortType, CursorRequest paging) {
        WalkwaySort sort = WalkwaySort.typeOf(sortType);
        CursorResponse<Walkway> walkways = switch (sort) {
            case LIKED -> walkwayRdbService.getWalkwaysLiked(memberId, paging.lastId(), paging.size());
            case RATING -> walkwayRdbService.getWalkwaysRating(memberId, paging.lastId(), paging.size());
            default -> walkwayRdbService.getWalkwaysLatest(memberId, paging.lastId(), paging.size());
        };
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();

        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        Set<Long> likedWalkwaySet = likedWalkwayRdbService.likedWalkways(memberId, walkwayIds);

        List<SearchWalkwayResponse> response = SearchWalkwayResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap, likedWalkwaySet);
        return new CursorResponse<>(response, walkways.getHasNext());
    }

}
