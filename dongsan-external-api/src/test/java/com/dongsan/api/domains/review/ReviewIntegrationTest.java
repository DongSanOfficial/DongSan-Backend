package com.dongsan.api.domains.review;

import com.dongsan.api.domains.review.dto.CreateReviewResponse;
import com.dongsan.api.domains.review.dto.MyReviewResponse;
import com.dongsan.api.domains.review.dto.WalkwayReviewsResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.ReviewFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.api.support.factory.WalkwayLogFactory;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.support.paging.CursorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private WalkwayFactory walkwayFactory;
    @Autowired
    private WalkwayLogFactory walkwayLogFactory;
    @Autowired
    private ReviewFactory reviewFactory;

    @Test
    void createReviewTest() {
        Long memberId = 1L;
        Long walkwayId = walkwayFactory.save();
        Long walkwayLogId = walkwayLogFactory.save(memberId, walkwayId, 1800, 3.0);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = String.format("""
                {
                    "walkwayLogId" : %d,
                    "content": "이 산책로 정말 좋아요!",
                    "rating": 5
                }
                """, walkwayLogId);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<CreateReviewResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/review",
                HttpMethod.POST,
                entity,
                CreateReviewResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().reviewId()).isNotNull();
    }

    @Test
    void getWalkwayReviewsTest() {
        Long memberId = 1L;
        Long walkwayId = walkwayFactory.save();
        Long walkwayLog1 = walkwayLogFactory.save(memberId, walkwayId, 1800, 3.0);
        Long walkwayLog2 = walkwayLogFactory.save(memberId, walkwayId, 1800, 3.0);
        reviewFactory.save(walkwayLog1, 4, "첫 번째 리뷰");
        reviewFactory.save(walkwayLog2, 5, "두 번째 리뷰");
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<WalkwayReviewsResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/review/content?sort=latest",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<WalkwayReviewsResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().data()).hasSize(2);
    }

    @Test
    void getWalkwaysRatingTest() {
        Long memberId = 1L;
        Long walkwayId = walkwayFactory.save();
        Long walkwayLog1 = walkwayLogFactory.save(memberId, walkwayId, 1800, 3.0);
        Long walkwayLog2 = walkwayLogFactory.save(memberId, walkwayId, 1800, 3.0);
        reviewFactory.save(walkwayLog1, 4, "첫 번째 리뷰");
        reviewFactory.save(walkwayLog2, 5, "두 번째 리뷰");
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ReviewStatistic> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/review/rating",
                HttpMethod.GET,
                entity,
                ReviewStatistic.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().rating()).isEqualTo(4.5);
    }

    @Test
    void getMyReviewsTest() {
        Long memberId = 1L;
        Long walkwayId1 = walkwayFactory.save();
        Long walkwayId2 = walkwayFactory.save();
        Long walkwayLog1 = walkwayLogFactory.save(memberId, walkwayId1, 1800, 3.0);
        Long walkwayLog2 = walkwayLogFactory.save(memberId, walkwayId2, 1800, 3.0);
        reviewFactory.save(walkwayLog1, 4, "첫 번째 리뷰");
        reviewFactory.save(walkwayLog2, 5, "두 번째 리뷰");
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<MyReviewResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/reviews?size=10",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<MyReviewResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().data()).hasSize(2);
    }

}
