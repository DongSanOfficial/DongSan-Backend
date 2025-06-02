package com.dongsan.api.domains.review;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.review.dto.CreateReviewRequest;
import com.dongsan.api.domains.review.dto.CreateReviewResponse;
import com.dongsan.api.domains.review.dto.WalkwayReviewsResponse;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로 리뷰")
@Validated
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @Autowired
    public ReviewController(ReviewFacade reviewFacade) {
        this.reviewFacade = reviewFacade;
    }

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{walkwayId}/review")
    public ResponseEntity<CreateReviewResponse> createReview(
            @PathVariable Long walkwayId,
            @Validated @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long reviewId = reviewFacade.createReview(customOAuth2User.getMemberId(), walkwayId, request);
        return ResponseEntity.ok(new CreateReviewResponse(reviewId));
    }

    @Operation(summary = "리뷰 내용 보기")
    @GetMapping("/{walkwayId}/review/content")
    public ResponseEntity<CursorResponse<WalkwayReviewsResponse>> getWalkwayReviews(
            @PathVariable Long walkwayId,
            @RequestParam String sort,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<ReviewWithMemberQuery> response
                = reviewFacade.getWalkwayReviews(sort, walkwayId, customOAuth2User.getMemberId(),
                new CursorRequest(lastId, size));
        return ResponseEntity.ok(
                new CursorResponse<>(WalkwayReviewsResponse.from(response.getData()), response.getHasNext()));
    }

    @Operation(summary = "리뷰 별점 보기")
    @GetMapping("/{walkwayId}/review/rating")
    public ResponseEntity<ReviewStatistic> getWalkwaysRating(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        ReviewStatistic reviewStatistic = reviewFacade.getWalkwayRating(walkwayId, customOAuth2User.getMemberId());
        return ResponseEntity.ok(reviewStatistic);
    }
}
