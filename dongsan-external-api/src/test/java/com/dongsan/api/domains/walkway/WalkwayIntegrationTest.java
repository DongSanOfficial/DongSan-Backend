package com.dongsan.api.domains.walkway;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

import com.dongsan.api.domains.walkway.dto.response.BookmarksWithMarkedWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.CourseImageIdResponse;
import com.dongsan.api.domains.walkway.dto.response.SearchWalkwayResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayDetailResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayHistoryResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwayIdResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.BookmarkFactory;
import com.dongsan.api.support.factory.ImageFactory;
import com.dongsan.api.support.factory.MarkedWalkwayFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.domain.support.paging.CursorResponse;
import com.dongsan.file.service.S3FileService;

public class WalkwayIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private WalkwayFactory walkwayFactory;
    @Autowired
    private ImageFactory imageFactory;
    @Autowired
    private BookmarkFactory bookmarkFactory;
    @Autowired
    private MarkedWalkwayFactory markedWalkwayFactory;
    @MockBean
    private S3FileService s3FileService;

    @Test
    void createWalkwayTest() {
        imageFactory.save();

        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "courseImageId": 1,
                    "name": "test",
                    "memo": "test memo",
                    "distance": 0.2,
                    "time": 300,
                    "hashtags": ["test"],
                    "exposeLevel": "PUBLIC",
                    "course": [
                        {
                          "latitude": 1,
                          "longitude": 1
                        },
                        {
                          "latitude": 2,
                          "longitude": 2
                        }
                    ]
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayIdResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways",
                HttpMethod.POST,
                entity,
                WalkwayIdResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void createWalkwayTest_image_not_exists() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "courseImageId": 1,
                    "name": "test",
                    "memo": "test memo",
                    "distance": 0.2,
                    "time": 300,
                    "hashtags": ["test"],
                    "exposeLevel": "PUBLIC",
                    "course": [
                        {
                          "latitude": 1,
                          "longitude": 1
                        },
                        {
                          "latitude": 2,
                          "longitude": 2
                        }
                    ]
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayIdResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways",
                HttpMethod.POST,
                entity,
                WalkwayIdResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createWalkwayCourseImageTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);

        // given: S3 업로드 호출 시 가짜 URL 반환
        given(s3FileService.saveFile(any(MultipartFile.class)))
                .willReturn("https://test/");

        // multipart/form-data 요청 준비
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource resource = new ByteArrayResource("fakeImage".getBytes()) {
            @Override
            public String getFilename() {
                return "course.png";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("courseImage", resource); // @RequestPart("courseImage")와 동일 이름

        // when
        ResponseEntity<CourseImageIdResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/walkways/image",
                new HttpEntity<>(body, headers),
                CourseImageIdResponse.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void updateWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "name": "update",
                    "memo": "update memo",
                    "hashtags": ["test", "update"],
                    "exposeLevel": "PRIVATE"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateWalkwayTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "name": "update",
                    "memo": "update memo",
                    "hashtags": ["test", "update"],
                    "exposeLevel": "PRIVATE"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteWalkwayTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<WalkwayDetailResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.GET,
                entity,
                WalkwayDetailResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void getWalkwayTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<WalkwayDetailResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId,
                HttpMethod.GET,
                entity,
                WalkwayDetailResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getBookmarksWithMarkedWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        markedWalkwayFactory.save(bookmarkId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<BookmarksWithMarkedWalkwayResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/bookmarks?size=10",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<BookmarksWithMarkedWalkwayResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(bookmarkId).isEqualTo(response.getBody().data().get(0).bookmarkId());
    }

    @Test
    void searchWalkwayTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<SearchWalkwayResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways?sort=liked&latitude=37.5&longitude=127.0&distance=2.0",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<SearchWalkwayResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(walkwayId).isEqualTo(response.getBody().data().get(0).walkwayId());
    }

    @Test
    void getWalkwaysLatest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<SearchWalkwayResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/all?sort=liked",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<SearchWalkwayResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(walkwayId).isEqualTo(response.getBody().data().get(0).walkwayId());
    }

    @Test
    void createHistoryTest() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "time": 10,
                    "distance": 0.3
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayHistoryResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/history",
                HttpMethod.POST,
                entity,
                WalkwayHistoryResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void createHistoryTest_walkway_not_exists() {
        imageFactory.save();
        Long walkwayId = 999L;
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "time": 10,
                    "distance": 0.3
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayHistoryResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/history",
                HttpMethod.POST,
                entity,
                WalkwayHistoryResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void createHistoryTest_wrong_time() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "time": -100,
                    "distance": 0.3
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayHistoryResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/history",
                HttpMethod.POST,
                entity,
                WalkwayHistoryResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void createHistoryTest_wrong_distance() {
        imageFactory.save();
        Long walkwayId = walkwayFactory.save();
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "time": 10,
                    "distance": -0.3
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<WalkwayHistoryResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/walkways/" + walkwayId + "/history",
                HttpMethod.POST,
                entity,
                WalkwayHistoryResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
