package com.dongsan.domain.domains.review.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReviewTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 6, -1})
    @DisplayName("별점이 1점 미만 5점 초과이면 예외가 발생한다")
    void shouldThrowException_whenRatingIsInvalid(int invalidRating) {
        assertThatThrownBy(() -> new Review(1L, invalidRating, "내용"))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.INVALID_RATING_VALUE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("리뷰 내용이 공백이면 예외가 발생한다")
    void shouldThrowException_whenContentIsBlank(String invalidContent) {
        assertThatThrownBy(() -> new Review(1L, 1, invalidContent))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.REVIEW_CONTENT_BLANK);
    }

    @Test
    @DisplayName("리뷰 내용이 공백이면 예외가 발생한다")
    void shouldThrowException_whenContentIsGt200() {
        String invalidContent = "a".repeat(201);

        assertThatThrownBy(() -> new Review(1L, 1, invalidContent))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.REVIEW_CONTENT_GT_200);
    }

}
