package com.dongsan.domain.domains.crew.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetaCrewRankingTest {
    @Test
    @DisplayName("새로운 MetaCrewRanking의 logCount = 0으로 초기화한다")
    void shouldCreateNewMetaCrewRankingWithZeroLogCount() {
        Long crewId = 1L;

        MetaCrewRanking metaCrewRanking = new MetaCrewRanking(crewId);

        assertThat(metaCrewRanking.getCrewId()).isEqualTo(crewId);
        assertThat(metaCrewRanking.getLogCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("logCount에 값을 더할 수 있다")
    void shouldAddLogCount() {
        MetaCrewRanking metaCrewRanking = new MetaCrewRanking(1L);
        Long initialCount = metaCrewRanking.getLogCount();
        Long countToAdd = 5L;

        metaCrewRanking.addLogCount(countToAdd);

        assertThat(metaCrewRanking.getLogCount()).isEqualTo(initialCount + countToAdd);
    }

}
