package com.dongsan.api.domains.cowalk.dto.request;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCowalkPostRequest(
        @NotNull(message = "산책 날짜를 입력해주세요")
        LocalDate date,

        @Schema(type = "string", example = "08:00:00", description = "산책 시작 시간")
        @NotNull(message = "산책 시간을 입력해주세요")
        LocalTime time,

        @NotNull(message = "인원 제한 여부를 입력해주세요")
        Boolean limitEnable,

        @Range(min = 2, max = 100, message = "인원은 2~100명까지 가능합니다")
        Integer memberLimit,

        @Size(max = 200, message = "메모는 200자를 넘길 수 없습니다.")
        String memo
) {
    public CreateCowalkPostCommand toCreateCowalkPostCommand(Long crewId, Long memberId) {
        return new CreateCowalkPostCommand(
                crewId,
                memberId,
                date,
                time,
                memberLimit,
                memo
        );
    }
}
