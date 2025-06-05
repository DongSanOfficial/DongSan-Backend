package com.dongsan.api.domains.crew.dto.request;

import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;
import com.dongsan.domain.domains.crew.service.CrewInfoCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUpdateCrewRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하 입니다.")
        String name,

        @Size(max = 250, message = "설명은 최대 250자 입니다.")
        String description,

        @Size(max = 250, message = "설명은 최대 250자 입니다.")
        String rule,

        @NotNull(message = "크루 공개 여부는 필수 입니다.")
        CrewExposeLevel visibility,

        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
        String password,

        @NotNull(message = "크루 가입 제한 여부는 필수 입니다.")
        boolean limitEnable,

        @Size(min = 2, max = 100)
        Integer memberLimit,

        Long crewImageId
) {

    public CreateUpdateCrewRequest {
        name = trimToNull(name);
        description = trimToNull(description);
        rule = trimToNull(rule);
        password = trimToNull(password);
    }

    private static String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public CrewInfoCommand toCrewInfoCommand(String imageUrl) {
        return new CrewInfoCommand(
                name, description, rule, visibility, password, limitEnable, memberLimit, imageUrl
        );
    }
}
