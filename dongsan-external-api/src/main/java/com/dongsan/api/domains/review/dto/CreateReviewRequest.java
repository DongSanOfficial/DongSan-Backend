package com.dongsan.api.domains.review.dto;

import com.dongsan.api.support.validation.ValidRating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateReviewRequest(
        @NotNull
        Long walkwayLogId,

        @NotNull(message = "별점을 입력해주세요.")
        @ValidRating
        int rating,

        @NotBlank(message = "리뷰 내용을 입력해주세요.")
        @Length(min = 1, max = 200)
        String content
) {
}
