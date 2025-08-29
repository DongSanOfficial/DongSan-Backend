package com.dongsan.api.domains.auth;

import com.dongsan.api.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthIntegrationTest extends IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String originalAccessToken;
    private String originalRefreshToken;

    @BeforeEach
    void setUp() {
        Long memberId = 1L;

        String jsonBody = String.format("{\"memberId\": %d}", memberId);
        HttpHeaders devTokenHeaders = new HttpHeaders();
        devTokenHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> initialResponse = restTemplate.exchange(
                "http://localhost:" + port + "/dev/token",
                HttpMethod.POST,
                new HttpEntity<>(jsonBody, devTokenHeaders),
                Void.class
        );

        List<String> cookies = initialResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

        originalAccessToken = cookies.stream()
                .filter(cookie -> cookie.startsWith("accessToken="))
                .findFirst()
                .map(cookie -> cookie.substring("accessToken=".length(), cookie.indexOf(";")))
                .orElseThrow();
        originalRefreshToken = cookies.stream()
                .filter(cookie -> cookie.startsWith("refreshToken="))
                .findFirst()
                .map(cookie -> cookie.substring("refreshToken=".length(), cookie.indexOf(";")))
                .orElseThrow();

        System.out.println("old: " + originalAccessToken);
    }

    @Test
    void renewTokenTest() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE,
                "accessToken=" + originalAccessToken + "; refreshToken=" + originalRefreshToken);

        Thread.sleep(1000);

        ResponseEntity<Void> refreshResponse = restTemplate.exchange(
                "http://localhost:" + port + "/auth/refresh",
                HttpMethod.POST,
                new HttpEntity<>(null, headers),
                Void.class
        );

        assertEquals(HttpStatus.OK, refreshResponse.getStatusCode());

        String newAccessToken = refreshResponse.getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .filter(cookie -> cookie.startsWith("accessToken="))
                .findFirst()
                .map(cookie -> cookie.substring("accessToken=".length(), cookie.indexOf(";")))
                .orElseThrow();

        assertNotEquals(originalAccessToken, newAccessToken);
    }


    @Test
    void logoutTest() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE,
                "accessToken=" + originalAccessToken + "; refreshToken=" + originalRefreshToken);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/auth/logout",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders responseHeaders = response.getHeaders();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE)).isNotNull();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(cookie -> cookie.contains("accessToken=; Max-Age=0"))).isTrue();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(cookie -> cookie.contains("refreshToken=; Max-Age=0"))).isTrue();
    }

    @Test
    void deactivateTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE,
                "accessToken=" + originalAccessToken + "; refreshToken=" + originalRefreshToken);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/auth/logout",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders responseHeaders = response.getHeaders();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE)).isNotNull();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(cookie -> cookie.contains("accessToken=; Max-Age=0"))).isTrue();
        assertThat(responseHeaders.get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(cookie -> cookie.contains("refreshToken=; Max-Age=0"))).isTrue();
    }


}
