package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.walkway.dto.response.GetWalkwayHistoriesResponse;
import com.dongsan.api.domains.walkway.dto.response.MyWalkwayResponse;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.rdb.domains.walkway.Walkway;
import com.dongsan.rdb.domains.walkway.service.WalkwayService;
import com.dongsan.rdb.support.util.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/walkways")
@Tag(name = "마이페이지")
public class UserWalkwayController {
    private final WalkwayService walkwayService;

    public UserWalkwayController(WalkwayService walkwayService) {
        this.walkwayService = walkwayService;
    }

    // 🌈

    /**
     * 내가 등록한 산책로 조회
     *
     * @param size             한번에 몇개 조회할 건지
     * @param lastId           마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "등록한 산책로 조회")
    @GetMapping("/upload")
    public ResponseEntity<CursorResponse<MyWalkwayResponse>> getUserUploadWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<Walkway> response = walkwayService.getUserWalkway(customOAuth2User.getMemberId(), size, lastId);
        return ResponseEntity.ok(new CursorResponse<>(MyWalkwayResponse.from(response.data()), response.hasNext()));
    }

    // 🌈

    /**
     * 내가 좋아요한 산책로 상세 보기
     *
     * @param size             한번에 몇개 조회할 건지
     * @param lastId           마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "좋아요한 산책로 조회")
    @GetMapping("/like")
    public ResponseEntity<CursorResponse<MyWalkwayResponse>> getUserLikedWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<Walkway> response = walkwayService.getUserLikedWalkway(customOAuth2User.getMemberId(), size,
                lastId);
        return ResponseEntity.ok(new CursorResponse<>(MyWalkwayResponse.from(response.data()), response.hasNext()));
    }

    // 🌈
    @Operation(summary = "회원의 리뷰 작성 가능한 산책로 이용 기록 모두 보기")
    @GetMapping("/history")
    public ResponseEntity<GetWalkwayHistoriesResponse> getUserWalkwayHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<WalkwayHistory> response = walkwayService.getUserCanReviewWalkwayHistory(
                customOAuth2User.getMemberId(), lastId, size);
        return ResponseEntity.ok(GetWalkwayHistoriesResponse.from(response.data(), response.hasNext()));
    }

    // 나의 그동안 산책한 내역 조회

    // 나의 그동안 산책한 내역 with (리뷰 작성 가능 여뷰, 리뷰를 작성했다면 그 id까지)

}
