package com.dongsan.api.domains.review;

import org.hibernate.validator.constraints.Length;

import com.dongsan.api.support.validation.ValidRating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewRequest(
	@NotNull
	Long walkwayHistoryId,
	@NotNull(message = "별점을 입력해주세요.")
	@ValidRating
	Integer rating,
	@NotBlank(message = "리뷰 내용을 입력해주세요.")
	@Length(min = 1, max = 100)
	String content
) {
}
