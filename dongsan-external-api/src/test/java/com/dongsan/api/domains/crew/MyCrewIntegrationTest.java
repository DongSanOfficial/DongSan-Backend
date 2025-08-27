package com.dongsan.api.domains.crew;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dongsan.api.domains.crew.dto.response.GetCrewsResponse;
import com.dongsan.api.domains.crew.dto.response.GetMyCrewIdsResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.CrewFactory;
import com.dongsan.api.support.factory.ImageFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.api.support.factory.WalkwayLogFactory;
import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;
import com.dongsan.domain.support.paging.CursorResponse;
import com.dongsan.file.service.S3FileService;

class MyCrewIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private CrewFactory crewFactory;
    @Autowired
    private WalkwayFactory walkwayFactory;
    @Autowired
    private ImageFactory imageFactory;
    @Autowired
    private WalkwayLogFactory walkwayLogFactory;
    @MockBean
    private S3FileService s3FileService;

    @Test
    void getMyCrewsTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetCrewsResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/crews",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetCrewsResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void getMyCrewIdsTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GetMyCrewIdsResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/crews/ids",
                HttpMethod.GET,
                entity,
                GetMyCrewIdsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().crewIds().size());
    }
}
