package com.dongsan.api.domains.cowalk;

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

import com.dongsan.api.domains.cowalk.dto.response.GetMyCowalkResponse;
import com.dongsan.api.support.IntegrationTest;
import com.dongsan.api.support.TestAuthHelper;
import com.dongsan.api.support.factory.CowalkParticipantFactory;
import com.dongsan.api.support.factory.CowalkPostFactory;
import com.dongsan.api.support.factory.CrewFactory;
import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;
import com.dongsan.domain.support.paging.CursorResponse;

class MyCowalkIntegrationTest extends IntegrationTest {
    @Autowired
    private TestAuthHelper authHelper;
    @Autowired
    private CrewFactory crewFactory;
    @Autowired
    private CowalkPostFactory cowalkPostFactory;
    @Autowired
    private CowalkParticipantFactory cowalkParticipantFactory;

    @Test
    void getMyCowalkTest() {
        Long memberId = 1L;
        HttpHeaders headers = authHelper.generateTokenHeader(memberId);
        Long crewId = crewFactory.save(CrewExposeLevel.PUBLIC, memberId);
        cowalkPostFactory.save(crewId, memberId);
        cowalkParticipantFactory.save(1L, memberId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CursorResponse<GetMyCowalkResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/cowalk",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CursorResponse<GetMyCowalkResponse>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().data().size());
        Assertions.assertNotNull(response.getBody());
    }
}
