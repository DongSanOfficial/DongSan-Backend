package com.dongsan.domain.domains.walkway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MetaWalkwayRatingTest {
    @Test
    @DisplayName("walkway의 초기 평점은 0.0 이다")
    void shouldReturnZero_whenNoRatingsExist() {
        MetaWalkwayRating rating = new MetaWalkwayRating(1L);

        assertThat(rating.getRating()).isZero();
    }

    @Test
    @DisplayName("평점을 추가할 때 평균 평점, 총합, 개수가 올바르게 업데이트된다")
    void shouldAddRatingAndCorrectlyUpdateValues() {
        MetaWalkwayRating rating = new MetaWalkwayRating(1L);

        rating.addRating(5);

        assertThat(rating.getRating()).isEqualTo(5.0);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, -1})
    @DisplayName("유효하지 않은 평점(1미만, 5초과)은 무시된다")
    void shouldIgnoreInvalidRatings(int invalidRating) {
        MetaWalkwayRating rating = new MetaWalkwayRating(1L);
        rating.addRating(5);

        rating.addRating(invalidRating);

        assertThat(rating.getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("getRating()이 올바른 평균 평점을 계산한다")
    void shouldCalculateCorrectAverageRating() {
        MetaWalkwayRating rating = new MetaWalkwayRating(1L);

        rating.addRating(5);
        rating.addRating(3);
        rating.addRating(4);

        assertThat(rating.getRating()).isEqualTo(4.0);
    }

}
