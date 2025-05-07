package com.dongsan.api.domains;

import com.dongsan.api.domains.member.MemberProfileResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    TestAuthHelper authHelper;

    @Test
    void getProfileTest_existMember() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<MemberProfileResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/profile",
                HttpMethod.GET,
                entity,
                MemberProfileResponse.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertEquals(response.getBody().nickname(), "테스트유저");
    }

    @Test
    void patchNicknameTest_blankNickname() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "nickname": "   "
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/profile/nickname",
                HttpMethod.PATCH,
                entity,
                Void.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void patchNicknameTest_validNickname() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        String jsonBody = """
                {
                    "nickname": "새닉네임"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/profile/nickname",
                HttpMethod.PATCH,
                entity,
                Void.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
