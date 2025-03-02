package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.domains.walkway.dto.response.GetWalkwayHistoriesResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayListResponse;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

    /**
     * 내가 등록한 산책로 조회
     * @param size 한번에 몇개 조회할 건지
     * @param lastId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "등록한 산책로 조회")
    @GetMapping("/upload")
    public ApiResponse<WalkwayListResponse> getUserUploadWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        List<Walkway> response = walkwayService.getUserWalkway(customOAuth2User.getMemberId(), size, lastId);
        return ApiResponse.success(WalkwayListResponse.from(response, response.size() == size));
    }

    /**
     * 내가 좋아요한 산책로 상세 보기
     * @param size 한번에 몇개 조회할 건지
     * @param lastId 마지막에 조회한 walkway의 id
     * @param customOAuth2User header의 access Token 를 통해 가지고 온 사용자 정보
     */
    @Operation(summary = "좋아요한 산책로 조회")
    @GetMapping("/like")
    public ApiResponse<WalkwayListResponse> getUserLikedWalkway(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        List<Walkway> response = walkwayService.getUserLikedWalkway(customOAuth2User.getMemberId(), size, lastId);
        return ApiResponse.success(WalkwayListResponse.from(response, response.size() == size));
    }

    @Operation(summary = "회원의 리뷰 작성 가능한 산책로 이용 기록 모두 보기")
    @GetMapping("/history")
    public ApiResponse<GetWalkwayHistoriesResponse> getUserWalkwayHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        List<WalkwayHistory> walkwayHistories = walkwayService.getUserCanReviewWalkwayHistory(customOAuth2User.getMemberId(), lastId, size);
        return ApiResponse.success(GetWalkwayHistoriesResponse.from(walkwayHistories));
    }

}
