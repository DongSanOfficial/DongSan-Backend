package com.dongsan.api.domains.crew;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.api.domains.crew.dto.response.CreateCrewResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewFeedResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewInfoResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewMemberRankingResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewsResponse;
import com.dongsan.api.domains.crew.dto.response.IsNameUniqueResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.CrewFactory;
import com.dongsan.api.support.factory.ImageFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.api.support.factory.WalkwayLogFactory;
import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import com.dongsan.domain.support.paging.CursorResponse;
import com.dongsan.file.service.S3FileService;

public class CrewIntegrationTest extends IntegrationTest {
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
    void isNameUniqueTest_unique() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<IsNameUniqueResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/exists?crewId=1&name=test",
                HttpMethod.GET,
                entity,
                IsNameUniqueResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().isValid()).isTrue();
    }

    @Test
    void isNameUniqueTest_not_unique() {
        Long memberId = 1L;
        Long otherMemberId = 2L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        crewFactory.save("중복이름", CrewExposeLevel.PUBLIC, otherMemberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<IsNameUniqueResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/exists?crewId=1&name=중복이름",
                HttpMethod.GET,
                entity,
                IsNameUniqueResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().isValid()).isFalse();
    }


    @Test
    void isNameUniqueTest_crew_not_exists() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<IsNameUniqueResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/exists?crewId=999&name=test",
                HttpMethod.GET,
                entity,
                IsNameUniqueResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void getCrewInfoTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        imageFactory.save("https://example.com/crew.png");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GetCrewInfoResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/"+ crewId +"/info",
                HttpMethod.GET,
                entity,
                GetCrewInfoResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getCrewInfoTest_crew_not_exists() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        Long crewId = 999L;
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GetCrewInfoResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/"+ crewId +"/info",
                HttpMethod.GET,
                entity,
                GetCrewInfoResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCrewFeedTest() {
        Long memberId = 1L;
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        walkwayLogFactory.save(memberId, walkwayId, 300, 0.2);

        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetCrewFeedResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/feeds",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetCrewFeedResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void getCrewMemberRankingTest() {
        Long memberId = 1L;
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        walkwayLogFactory.save(memberId, walkwayId, 300, 0.2);

        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetCrewMemberRankingResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/ranking?period=daily&date=" + LocalDate.now() + "&sort=distance",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetCrewMemberRankingResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void createCrewTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        String jsonBody = """
                {
                    "name": "테스트 크루",
                    "description": "크루 설명",
                    "rule": "크루 규칙",
                    "visibility": "PUBLIC",
                    "password": "12345678",
                    "limitEnable": true,
                    "memberLimit": 50,
                    "crewImageId": null
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<CreateCrewResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews",
                HttpMethod.POST,
                entity,
                CreateCrewResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createCrewImage() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        given(s3FileService.saveFile(any(MultipartFile.class)))
                .willReturn("https://test/");

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource resource = new ByteArrayResource("fakeImage".getBytes()) {
            @Override
            public String getFilename() {
                return "crew.png";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("crewImage", resource);

        ResponseEntity<CreateCrewImageResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/crews/image",
                new HttpEntity<>(body, headers),
                CreateCrewImageResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void joinCrewTest() {
        Long managerMemberId = 2L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, managerMemberId);

        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        String jsonBody = """
                {
                    "password": "1234"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/members",
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateCrewTest() {
        Long memberId = 1L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        String jsonBody = """
                {
                    "name": "이름변경",
                    "description": "설명변경",
                    "rule": "규칙변경",
                    "visibility": "PUBLIC",
                    "password": "12345678",
                    "limitEnable": true,
                    "memberLimit": 50,
                    "crewImageId": null
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void leaveCrewTest() {
        Long managerMemberId = 2L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, managerMemberId);

        Long memberId = 1L;
        crewFactory.saveMember(memberId, crewId, CrewMemberRole.MEMBER);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/" + crewId + "/members",
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void searchCrewsTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        imageFactory.save("https://example.com/crew.png");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetCrewsResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/search?name=테스트",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetCrewsResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void recommendCrewsTest() {
        Long memberId = 1L;
        imageFactory.save();

        Long crewManagerId = 2L;
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, crewManagerId);
        crewFactory.saveMetaCrewRanking(crewId, 10L);

        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetCrewsResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/crews/recommend",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetCrewsResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
    }
}
