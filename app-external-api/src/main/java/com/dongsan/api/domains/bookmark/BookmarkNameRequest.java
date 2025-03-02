package com.dongsan.api.domains.bookmark;

import jakarta.validation.constraints.NotBlank;

public record BookmarkNameRequest(
        @NotBlank(message = "북마크 제목을 입력해주세요.")
        String name
) {
}
