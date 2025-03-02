package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.walkway.WalkwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/walkways")
@Tag(name = "산책로")
@Validated
public class LikedWalkwayController {
    @Autowired
    public LikedWalkwayController(WalkwayService walkwayService) {
        this.walkwayService = walkwayService;
    }

    private final WalkwayService walkwayService;

    @Operation(summary = "산책로 좋아요")
    @PostMapping("/{walkwayId}/likes")
    public ApiResponse<Void> createLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        walkwayService.createLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "산책로 좋아요 취소")
    @DeleteMapping("/{walkwayId}/likes")
    public ApiResponse<Void> deleteLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        walkwayService.deleteLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ApiResponse.success(null);
    }
}
