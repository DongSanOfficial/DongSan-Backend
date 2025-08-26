package com.dongsan.api.domains.walkway;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dongsan.api.domains.walkway.dto.response.WalkwayLogWithReviewResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwaySimpleResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.ImageFactory;
import com.dongsan.api.support.factory.LikedWalkwayFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.api.support.factory.WalkwayLogFactory;
import com.dongsan.domain.support.paging.CursorResponse;

public class UserWalkwayIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private WalkwayFactory walkwayFactory;
    @Autowired
    private ImageFactory imageFactory;
    @Autowired
    private LikedWalkwayFactory likedWalkwayFactory;
    @Autowired
    private WalkwayLogFactory walkwayLogFactory;

    @Test
    void getUserUploadWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<WalkwaySimpleResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/walkways/upload",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<WalkwaySimpleResponse>>() {
                }
        );

        int expectedSize = 1;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(expectedSize).isEqualTo(response.getBody().data().size());
    }

    @Test
    void getUserLikedWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        likedWalkwayFactory.save(memberId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<WalkwaySimpleResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/walkways/like",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<WalkwaySimpleResponse>>() {
                }
        );

        int expectedSize = 1;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(expectedSize).isEqualTo(response.getBody().data().size());
    }

    @Test
    void getUserWalkwayHistoryTest() {
        imageFactory.save();
        Long walkwayId1 = walkwayFactory.save();
        Long walkwayId2 = walkwayFactory.save();
        Long memberId = 1L;
        walkwayLogFactory.save(memberId, walkwayId1, 300, 0.2);
        walkwayLogFactory.save(memberId, walkwayId2, 300, 0.1);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<WalkwayLogWithReviewResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/walkways/history",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<WalkwayLogWithReviewResponse>>() {
                }
        );

        int expectedSize = 2;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(expectedSize).isEqualTo(response.getBody().data().size());
        assertThat(response.getBody().data().get(0).canReview()).isFalse();
        assertThat(response.getBody().data().get(1).canReview()).isTrue();
    }
}
