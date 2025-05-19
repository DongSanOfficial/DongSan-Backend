package com.dongsan.api.domains.review;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.review.dto.MyReviewResponse;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.rdb.support.util.CursorRequest;
import com.dongsan.rdb.support.util.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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

    public UserReviewController(UserReviewService userReviewService) {
        this.userReviewService = userReviewService;
    }

    /**
     * 작성한 리뷰 전체 보기
     */
    @Operation(summary = "내가 작성한 리뷰 보기")
    @GetMapping()
    public ResponseEntity<CursorResponse<MyReviewResponse>> getReviews(
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        PagingResponse<Review> response = userReviewService.getReviews(new CursorRequest(lastId, size),
                customOAuth2User.getMemberId());
        return ResponseEntity.ok(new CursorResponse<>(MyReviewResponse.from(response.data()), response.hasNext()));
    }

}
