package com.dongsan.api.domains.member;

import jakarta.validation.constraints.NotBlank;

public record PatchNicknameRequest(
        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        String nickname
) {
}
