package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.walkway.dto.response.WalkwayLogWithReviewResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwaySimpleResponse;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
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
    private final UserWalkwayFacade userWalkwayFacade;

    public UserWalkwayController(UserWalkwayFacade userWalkwayFacade) {
        this.userWalkwayFacade = userWalkwayFacade;
    }

    @Operation(summary = "등록한 산책로 조회")
    @GetMapping("/upload")
    public ResponseEntity<CursorPage<WalkwaySimpleResponse>> getUserUploadWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorPage<WalkwaySimpleResponse> response = userWalkwayFacade.getUserWalkway(customOAuth2User.getMemberId(), new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "좋아요한 산책로 조회")
    @GetMapping("/like")
    public ResponseEntity<CursorPage<WalkwaySimpleResponse>> getUserLikedWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorPage<WalkwaySimpleResponse> response = userWalkwayFacade.getUserLikedWalkway(customOAuth2User.getMemberId(), new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    
    @Operation(summary = "산책로 이용 내역 및 리뷰 작성 여부 조회")
    @GetMapping("/history")
    public ResponseEntity<CursorPage<WalkwayLogWithReviewResponse>> getUserWalkwayHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorPage<WalkwayLogWithReviewResponse> response = userWalkwayFacade.getUserWalkwayHistoryWithReview(
                customOAuth2User.getMemberId(), new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }


}
