package com.dongsan.api.domains.review;

import com.dongsan.api.domains.walkway.dto.request.CreateReviewRequest;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.review.CreateReview;
import com.dongsan.core.domains.review.Review;
import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.core.domains.review.ReviewService;
import com.dongsan.core.support.util.CursorPagingRequest;
import com.dongsan.core.support.util.CursorPagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로 리뷰")
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{walkwayId}/review")
    public ApiResponse<CreateReviewResponse> createReview(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        CreateReview createReview
                = new CreateReview(customOAuth2User.getMemberId(), walkwayId, request.walkwayHistoryId(), request.rating(), request.content());
        Long reviewId = reviewService.createReview(createReview);
        return ApiResponse.success(new CreateReviewResponse(reviewId));
    }

    @Operation(summary = "리뷰 내용 보기")
    @GetMapping("/{walkwayId}/review/content")
    public ApiResponse<GetWalkwayReviewsResponse> getWalkwayReviews(
            @PathVariable Long walkwayId,
            @RequestParam String sort,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        CursorPagingResponse<Review> cursorPagingResponse
                = reviewService.getWalkwayReviews(sort, walkwayId, customOAuth2User.getMemberId(), new CursorPagingRequest(lastId, size));
        return ApiResponse.success(new GetWalkwayReviewsResponse(cursorPagingResponse));
    }

    @Operation(summary = "리뷰 별점 보기")
    @GetMapping("/{walkwayId}/review/rating")
    public ApiResponse<GetWalkwayRatingResponse> getWalkwaysRating(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Map<Integer, Long> ratingCounts = reviewService.getWalkwayRating(walkwayId, customOAuth2User.getMemberId());
        return ApiResponse.success(GetWalkwayRatingResponse.from(ratingCounts));
    }
}
