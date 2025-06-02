package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.response.*;
import com.dongsan.domain.domains.bookmark.infrastructure.dto.BookmarkWithMarkedStatus;
import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로")
@Validated
public class WalkwayController {

    private final WalkwayFacade walkwayFacade;

    public WalkwayController(WalkwayFacade walkwayFacade) {
        this.walkwayFacade = walkwayFacade;
    }

    @Operation(summary = "산책로 등록")
    @PostMapping("")
    public ResponseEntity<WalkwayIdResponse> createWalkway(
            @Validated @RequestBody CreateWalkwayRequest createWalkwayRequest,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long walkwayId = walkwayFacade.createWalkway(createWalkwayRequest, customOAuth2User.getMemberId());
        return ResponseEntity.ok(new WalkwayIdResponse(walkwayId));
    }

    @Operation(summary = "산책로 코스 이미지 등록")
    @PostMapping(value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseImageIdResponse> createWalkwayCourseImage(
            @RequestPart("courseImage") MultipartFile courseImage
    ) {
        Long imageId = walkwayFacade.saveImage(courseImage);
        return ResponseEntity.ok(new CourseImageIdResponse(imageId));
    }

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

    @Operation(summary = "산책로 단건 조회")
    @GetMapping("/{walkwayId}")
    public ResponseEntity<WalkwayDetailResponse> getWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        WalkwayDetailResponse response = walkwayFacade.getWalkwayDetail(walkwayId, customOAuth2User.getMemberId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "북마크 목록 보기(산책로 마크 여부 포함)")
    @GetMapping("/{walkwayId}/bookmarks")
    public ResponseEntity<CursorResponse<BookmarksWithMarkedWalkwayResponse>> getBookmarksWithMarkedWalkway(
            @PathVariable Long walkwayId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<BookmarkWithMarkedStatus> response
                = walkwayFacade.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, new CursorRequest(lastId, size));
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
                = new SearchWalkwayQuery(customOAuth2User.getMemberId(), sort, longitude, latitude, distance);
        CursorResponse<SearchWalkwayResponse> response = walkwayFacade.searchWalkway(searchWalkwayQuery, new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "산책로 조회 (위치 기반 X)")
    @GetMapping("/all")
    public ResponseEntity<CursorResponse<SearchWalkwayResponse>> getWalkwaysLatest(
            @RequestParam(name = "sort", defaultValue = "latest") String sort,
            @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<SearchWalkwayResponse> response = walkwayFacade.getWalkwaysLatest(customOAuth2User.getMemberId(), sort, new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "산책로 이용 기록")
    @PostMapping("/{walkwayId}/history")
    public ResponseEntity<WalkwayHistoryResponse> createHistory(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateWalkwayHistoryRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        WalkwayHistoryResponse response = walkwayFacade.createHistoryLog(walkwayId, customOAuth2User.getMemberId(), request.distance(), request.time());
        return ResponseEntity.ok(response);
    }

}
