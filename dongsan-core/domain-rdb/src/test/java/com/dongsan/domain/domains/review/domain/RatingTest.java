package com.dongsan.domain.domains.review.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RatingTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 6, 100})
    @DisplayName("유효하지 않은 숫자 값으로 Rating 객체를 찾으려 할 때 예외가 발생한다")
    void shouldThrowException_whenInvalidNumberIsGiven(Integer invalidNum) {
        assertThatThrownBy(() -> Rating.numOf(invalidNum))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.INVALID_RATING_VALUE);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,ONE", "2,TWO", "3,THREE", "4,FOUR", "5,FIVE"})
    @DisplayName("유효한 숫자 값으로 Rating 객체를 찾을 수 있다")
    void shouldReturnRating_whenValidNumberIsGiven(Integer num, Rating expectedRating) {
        Rating rating = Rating.numOf(num);

        assertThat(rating).isEqualTo(expectedRating);
    }

}
