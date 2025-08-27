package com.dongsan.api.domains.walkway;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.ImageFactory;
import com.dongsan.api.support.factory.LikedWalkwayFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.api.support.factory.WalkwayLogFactory;

class LikedWalkwayIntegrationTest extends IntegrationTest {
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
    void createLikedWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/likes",
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createLikedWalkwayTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/likes",
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteLikedWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        likedWalkwayFactory.save(memberId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/likes",
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteLikedWalkwayTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        likedWalkwayFactory.save(memberId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/likes",
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteLikedWalkwayTest_walkwayLiked_not_exists() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/likes",
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
