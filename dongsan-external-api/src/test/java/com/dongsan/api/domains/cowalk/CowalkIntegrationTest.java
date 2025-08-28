package com.dongsan.api.domains.cowalk;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dongsan.api.domains.cowalk.dto.response.CowalkCommentResponse;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostDetailResponse;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostsResponse;
import com.dongsan.api.domains.cowalk.dto.response.CreateCowalkCommentResponse;
import com.dongsan.api.domains.cowalk.dto.response.CreateCowalkPostResponse;
import com.dongsan.api.domains.cowalk.dto.response.JoinCowalkParticipantResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.CowalkCommentFactory;
import com.dongsan.api.support.factory.CowalkParticipantFactory;
import com.dongsan.api.support.factory.CowalkPostFactory;
import com.dongsan.api.support.factory.CrewFactory;
import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;
import com.dongsan.domain.support.paging.CursorResponse;

class CowalkIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private CrewFactory crewFactory;
    @Autowired
    private CowalkPostFactory cowalkPostFactory;
    @Autowired
    private CowalkParticipantFactory cowalkParticipantFactory;
    @Autowired
    private CowalkCommentFactory cowalkCommentFactory;

    @Test
    void createCowalkPostTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);

        String jsonBody = """
                {
                    "startDate": "%s",
                    "startTime": "%s",
                    "endTime": "%s",
                    "limitEnable": true,
                    "memberLimit": 10,
                    "memo": "같이산책 내용"
                }
                """.formatted(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1));

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<CreateCowalkPostResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk",
                HttpMethod.POST,
                entity,
                CreateCowalkPostResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void getCowalkPostTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CowalkPostDetailResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1",
                HttpMethod.GET,
                entity,
                CowalkPostDetailResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().cowalkId());
    }

    @Test
    void joinParticipantTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JoinCowalkParticipantResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1/join",
                HttpMethod.POST,
                entity,
                JoinCowalkParticipantResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void joinParticipantTest_crew_not_exists() {
        Long memberId = 1L;
        Long crewId = 999L;
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JoinCowalkParticipantResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1/join",
                HttpMethod.POST,
                entity,
                JoinCowalkParticipantResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void joinParticipantTest_cowalk_not_exists() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JoinCowalkParticipantResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/999/join",
                HttpMethod.POST,
                entity,
                JoinCowalkParticipantResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCowalkPostsTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<CowalkPostsResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<CowalkPostsResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void createCowalkCommentTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        cowalkParticipantFactory.save(1L, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        String jsonBody = """
                {
                    "content": "오홍홍 좋아요"
                }
        """;

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<CreateCowalkCommentResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1/comments",
                HttpMethod.POST,
                entity,
                CreateCowalkCommentResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createCowalkCommentTest_not_participant() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        String jsonBody = """
                {
                    "content": "오홍홍 좋아요"
                }
        """;

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<CreateCowalkCommentResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1/comments",
                HttpMethod.POST,
                entity,
                CreateCowalkCommentResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getCowalkCommentsTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        cowalkParticipantFactory.save(1L, memberId);
        cowalkCommentFactory.save(1L, memberId);

        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<CowalkCommentResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/cowalk/1/comments",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<CowalkCommentResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }
}
