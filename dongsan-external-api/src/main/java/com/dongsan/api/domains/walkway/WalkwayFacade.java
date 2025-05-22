package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.WalkwayDetailResponse;
import com.dongsan.file.service.S3FileService;
import com.dongsan.rdb.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.rdb.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.rdb.domains.image.ImageRdbService;
import com.dongsan.rdb.domains.walkway.CreateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayService;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
public class WalkwayFacade {
    private final WalkwayRdbService walkwayRdbService;
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final BookmarkRdbService bookmarkRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;

    public WalkwayFacade(WalkwayRdbService walkwayRdbService, WalkwayService walkwayService, LikedWalkwayRdbService likedWalkwayRdbService, BookmarkRdbService bookmarkRdbService, S3FileService s3FileService, ImageRdbService imageRdbService) {
        this.walkwayRdbService = walkwayRdbService;
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.bookmarkRdbService = bookmarkRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
    }

    public Long createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {
        String imageUrl = imageRdbService.getImage(createWalkwayRequest.courseImageId()).getUrl();
        CreateWalkwayCommand command = createWalkwayRequest.toCreateWalkwayCommand(imageUrl, memberId);
        return walkwayRdbService.save(command);
    }

    public void updateWalkway(UpdateWalkwayCommand command, Long memberId) {
        walkwayRdbService.update(command, memberId);
    }

    public void deleteWalkway(Long walkwayId, Long memberId) {
        walkwayRdbService.delete(walkwayId, memberId);
    }

    public Long saveImage(MultipartFile courseImage) {
        String imageUrl = s3FileService.saveFile(courseImage);
        return imageRdbService.save(imageUrl);
    }

    public CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long memberId, Long walkwayId,
                                                                              CursorRequest paging) {
        walkwayRdbService.getWalkway(walkwayId);
        LocalDateTime createdAt = bookmarkRdbService.getBookmarkCreatedAt(paging.lastId());
        return bookmarkRdbService.getBookmarksWithMarkedWalkway(walkwayId, memberId, createdAt, paging.size());
    }

    public WalkwayDetailResponse getWalkwayDetail(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayRdbService.getWalkwayWithAccessValidation(walkwayId, memberId);
        boolean isLike = likedWalkwayRdbService.isLiked(memberId, walkwayId);
        boolean isMarked = bookmarkRdbService.wasEverBookmarked(memberId, walkwayId);
        return new WalkwayDetailResponse(walkway, isLike, isMarked);
    }


}
