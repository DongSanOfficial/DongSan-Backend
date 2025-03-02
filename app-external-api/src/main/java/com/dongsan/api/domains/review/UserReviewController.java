package com.dongsan.api.domains.review;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.UserReviewService;
import com.dongsan.core.support.util.CursorPagingRequest;
import com.dongsan.core.support.util.CursorPagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/reviews")
@Tag(name = "마이페이지")
@Validated
public class UserReviewController {
    private final UserReviewService userReviewService;
    @Autowired
    public UserReviewController(UserReviewService userReviewService) {
        this.userReviewService = userReviewService;
    }

    /**
     * 작성한 리뷰 전체 보기
     */
    @Operation(summary = "내가 작성한 리뷰 보기")
    @GetMapping()
    public ApiResponse<GetReviewResponse> getReviews(
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        CursorPagingResponse<Review> cursorPagingResponse = userReviewService.getReviews(new CursorPagingRequest(lastId, size), customOAuth2User.getMemberId());
        return ApiResponse.success(GetReviewResponse.from(cursorPagingResponse));
    }

}
