package com.dongsan.api.domains.bookmark;

import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.BookmarkFactory;
import com.dongsan.api.support.factory.WalkwayFactory;
import com.dongsan.domain.support.paging.CursorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookmarkIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private BookmarkFactory bookmarkFactory;
    @Autowired
    private WalkwayFactory walkwayFactory;

    @Test
    void createBookmarkTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "name": "테스트 북마크"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<BookmarkIdResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks",
                HttpMethod.POST,
                entity,
                BookmarkIdResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void renameBookmarkTest() {
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "name": "새로운 북마크 이름"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks/" + bookmarkId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void includeWalkwayTest() {
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        Long walkwayId = walkwayFactory.save();
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "walkwayId": %d
                }
                """.formatted(walkwayId);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks/" + bookmarkId + "/walkways",
                HttpMethod.POST,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void excludeWalkwayTest() {
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        Long walkwayId = walkwayFactory.save();
        bookmarkFactory.includeWalkway(bookmarkId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks/" + bookmarkId + "/walkways/" + walkwayId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteBookmarkTest() {
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks/" + bookmarkId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBookmarkWalkwaysTest() {
        Long memberId = 1L;
        Long bookmarkId = bookmarkFactory.save(memberId);
        Long walkwayId = walkwayFactory.save();
        bookmarkFactory.includeWalkway(bookmarkId, walkwayId);
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<MarkedWalkwayResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/bookmarks/" + bookmarkId + "/walkways",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<MarkedWalkwayResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().data()).hasSize(1);
    }

    @Test
    void getBookmarksNameTest() {
        Long memberId = 1L;
        bookmarkFactory.save(memberId, "북마크1");
        bookmarkFactory.save(memberId, "북마크2");
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<BookmarksNameResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/bookmarks/title",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<BookmarksNameResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().data()).hasSize(2);
    }

}
