package com.dongsan.rdb.domains.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewTest {
    @Test
    @DisplayName("별점이 1점 미만 5점 초과이면 예외가 발생한다.")
    void validateRatingTest() {

    }

    @Test
    @DisplayName("리뷰 내용이 공백이면 예외가 발생한다.")
    void validateContentTest_blank() {

    }

    @Test
    @DisplayName("리뷰 내용이 200자 초과이면 예외가 발생한다.")
    void validateContentTest_gt200() {
    
    }

}
