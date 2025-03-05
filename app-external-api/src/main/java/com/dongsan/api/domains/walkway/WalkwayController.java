package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.CourseImageIdResponse;
import com.dongsan.api.domains.walkway.dto.response.GetWalkwayHistoriesResponse;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayDetailResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayHistoryResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayIdResponse;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.image.ImageService;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.CreateWalkwayHistory;
import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayService;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;
import com.dongsan.file.service.S3FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로")
@Validated
public class WalkwayController {

    private final WalkwayService walkwayService;
    private final BookmarkService bookmarkService;
    private final S3FileService s3FileService;
    private final ImageService imageService;

    @Autowired
    public WalkwayController(WalkwayService walkwayService, BookmarkService bookmarkService, S3FileService s3FileService, ImageService imageService) {
        this.walkwayService = walkwayService;
        this.bookmarkService = bookmarkService;
        this.s3FileService = s3FileService;
        this.imageService = imageService;
    }


    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<WalkwayIdResponse> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Image image = imageService.getImage(createWalkwayRequest.courseImageId());
        CreateWalkway createWalkway = createWalkwayRequest.toCreateWalkway(image, customOAuth2User.getMemberId());
        Long walkwayId = walkwayService.createWalkway(createWalkway);
        return ResponseEntity.ok(new WalkwayIdResponse(walkwayId));
    }

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value ="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseImageIdResponse> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        String imageUrl = s3FileService.saveFile(courseImage);
        Long imageId = imageService.createImage(imageUrl);
        return ResponseEntity.ok(new CourseImageIdResponse(imageId));
    }

    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<WalkwayDetailResponse> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Walkway walkway = walkwayService.getWalkway(customOAuth2User.getMemberId(), walkwayId);
        boolean isLike = walkwayService.existsLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        boolean isMarked = bookmarkService.existsMarkedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseEntity.ok(new WalkwayDetailResponse(walkway, isLike, isMarked));
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<CursorResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        PagingResponse<BookmarkWithMarkedStatus> response
                = bookmarkService.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, new CursorRequest(lastId, size));
        return ResponseEntity.ok(new CursorResponse<>(BookmarksWithMarkedWalkwayResponse.from(response.data()), response.hasNext()));
    }

    @Operation(summary = "산책로 수정")
    @PutMapping("/{walkwayId}")
    public ResponseEntity<Void> updateWalkway(
            @PathVariable Long walkwayId,
            @Validated @RequestBody UpdateWalkwayRequest updateWalkwayRequest,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        UpdateWalkway updateWalkway = updateWalkwayRequest.toUpdateWalkway(walkwayId);
        walkwayService.updateWalkway(updateWalkway, customOAuth2User.getMemberId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "산책로 검색")
    @GetMapping("")
    public ResponseEntity<CursorResponse<SearchWalkwayResponse>> searchWalkway(
            @RequestParam(name = "sort") String sort,
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "distance") Double distance,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        SearchWalkwayQuery searchWalkwayQuery
                = new SearchWalkwayQuery(customOAuth2User.getMemberId(), longitude, latitude, distance, lastId, size + 1);
        PagingResponse<Walkway> response = walkwayService.searchWalkway(sort, searchWalkwayQuery);

        List<Long> walkwayIds = response.data().stream()
                .map(Walkway::walkwayId)
                .toList();
        Map<Long, Boolean> isLiked = walkwayService.existsLikedWalkways(customOAuth2User.getMemberId(), walkwayIds);

        return ResponseEntity.ok(new CursorResponse<>(SearchWalkwayResponse.from(response.data(), isLiked),
                response.hasNext()));
    }

    @Operation(summary = "산책로 이용 기록")
    @PostMapping("/{walkwayId}/history")
    public ResponseEntity<WalkwayHistoryResponse> createHistory(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateWalkwayHistoryRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        CreateWalkwayHistory createWalkwayHistory
                = new CreateWalkwayHistory(walkwayId, customOAuth2User.getMemberId(), request.distance(), request.time());
        Long walkwayHistoryId = walkwayService.createWalkwayHistory(createWalkwayHistory);
        boolean canReview = walkwayService.isCanReview(walkwayHistoryId);
        return ResponseEntity.ok(new WalkwayHistoryResponse(walkwayHistoryId, canReview));
    }

    @Operation(summary = "리뷰 작성 가능한 산책로 이용 기록 보기")
    @GetMapping("/{walkwayId}/history")
    public ResponseEntity<GetWalkwayHistoriesResponse> getHistories(
            @PathVariable Long walkwayId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<WalkwayHistory> walkwayHistories
                = walkwayService.getCanReviewWalkwayHistory(walkwayId, customOAuth2User.getMemberId(), size, lastId);

        return ResponseEntity.ok(GetWalkwayHistoriesResponse.from(walkwayHistories));
    }
}
