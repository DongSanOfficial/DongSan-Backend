package com.dongsan.api.domains.cowalk.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record CreateCowalkCommentRequest(
        @NotBlank
        @Length(min = 1, max = 100)
        String content
) {
}
