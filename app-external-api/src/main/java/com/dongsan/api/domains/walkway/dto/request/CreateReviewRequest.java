package com.dongsan.api.domains.walkway.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateReviewRequest(
        @NotNull
        Long walkwayHistoryId,
        @NotNull(message = "별점을 입력해주세요.")
        @Min(1)
        @Max(5)
        Integer rating,
        @NotBlank(message = "리뷰 내용을 입력해주세요.")
        @Length(min = 1, max = 100)
        String content
) {
}
