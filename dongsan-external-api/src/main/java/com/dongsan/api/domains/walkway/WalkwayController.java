package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.*;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.rdb.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
import com.dongsan.rdb.support.util.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로")
@Validated
public class WalkwayController {

    private final WalkwayFacade walkwayFacade;

    public WalkwayController(WalkwayFacade walkwayFacade) {
        this.walkwayFacade = walkwayFacade;
    }

    // ✅
    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<WalkwayIdResponse> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long walkwayId = walkwayFacade.createWalkway(createWalkwayRequest, customOAuth2User.getMemberId());
        return ResponseEntity.ok(new WalkwayIdResponse(walkwayId));
    }

    // ✅
    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseImageIdResponse> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long imageId = walkwayFacade.saveImage(courseImage);
        return ResponseEntity.ok(new CourseImageIdResponse(imageId));
    }

    // ✅
    @Operation(summary = "산책로 수정")
    @PutMapping("/{walkwayId}")
    public ResponseEntity<Void> updateWalkway(
            @PathVariable Long walkwayId,
            @Validated @RequestBody UpdateWalkwayRequest updateWalkwayRequest,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        UpdateWalkwayCommand updateWalkwayCommand = updateWalkwayRequest.toUpdateWalkway(walkwayId);
        walkwayFacade.updateWalkway(updateWalkwayCommand, customOAuth2User.getMemberId());
        return ResponseEntity.ok()
                .build();
    }

    // ✅
    @Operation(summary = "산책로 삭제")
    @DeleteMapping("/{walkwayId}")
    public ResponseEntity<Void> deleteWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        walkwayFacade.deleteWalkway(walkwayId, customOAuth2User.getMemberId());
        return ResponseEntity.ok()
                .build();
    }

    // ✅
    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<WalkwayDetailResponse> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        WalkwayDetailResponse response = walkwayFacade.getWalkwayDetail(walkwayId, customOAuth2User.getMemberId());
        return ResponseEntity.ok(response);
    }

    // ✅
    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<CursorResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorPage<BookmarkWithMarkedStatus> response
                = walkwayFacade.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId,
                new CursorRequest(lastId, size));
        return ResponseEntity.ok(
                new CursorResponse<>(BookmarksWithMarkedWalkwayResponse.from(response.getData()), response.getHasNext()));
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
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        SearchWalkwayQuery searchWalkwayQuery
                = new SearchWalkwayQuery(customOAuth2User.getMemberId(), longitude, latitude, distance, lastId, size + 1);
        PagingResponse<Walkway> response = walkwayService.searchWalkway(sort, searchWalkwayQuery);

        List<Long> walkwayIds = response.data()
                .stream()
                .map(Walkway::walkwayId)
                .toList();
        Map<Long, Boolean> isLiked = walkwayService.existsLikedWalkways(customOAuth2User.getMemberId(), walkwayIds);

        return ResponseEntity.ok(new CursorResponse<>(SearchWalkwayResponse.from(response.data(), isLiked),
                response.hasNext()));
    }

    @Operation(summary = "산책로 조회 (위치 기반 X)")
    @GetMapping("/all")
    public ResponseEntity<CursorResponse<SearchWalkwayResponse>> getWalkwaysLatest(
            @RequestParam(name = "sort", defaultValue = "latest") String sort,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<Walkway> response = walkwayService.getWalkways(size, lastId,
                customOAuth2User.getMemberId(), sort);

        List<Long> walkwayIds = response.data()
                .stream()
                .map(Walkway::walkwayId)
                .toList();
        Map<Long, Boolean> isLiked = walkwayService.existsLikedWalkways(customOAuth2User.getMemberId(), walkwayIds);

        return ResponseEntity.ok(new CursorResponse<>(SearchWalkwayResponse.from(response.data(), isLiked),
                response.hasNext()));
    }

    // #### 산책로 이용 기록 ####
    @Operation(summary = "산책로 이용 기록")
    @PostMapping("/{walkwayId}/history")
    public ResponseEntity<WalkwayHistoryResponse> createHistory(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateWalkwayHistoryRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
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
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<WalkwayHistory> response
                = walkwayService.getCanReviewWalkwayHistory(walkwayId, customOAuth2User.getMemberId(), size, lastId);

        return ResponseEntity.ok(GetWalkwayHistoriesResponse.from(response.data(), response.hasNext()));
    }


}
