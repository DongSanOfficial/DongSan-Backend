package com.dongsan.domain.domains.bookmark.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarkedWalkwayTest {
    @Test
    @DisplayName("북마크된 산책로를 생성한다")
    void shouldCreateMarkedWalkway() {
        Long bookmarkId = 1L;
        Long walkwayId = 100L;

        MarkedWalkway markedWalkway = new MarkedWalkway(bookmarkId, walkwayId);

        assertThat(markedWalkway.getWalkwayId()).isEqualTo(walkwayId);
    }

}
