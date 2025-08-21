package com.dongsan.api.domains.walkway.dto.request;

import com.dongsan.domain.domains.walkway.CreateWalkwayCommand;
import com.dongsan.domain.domains.walkway.WalkwayCoordinate;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateWalkwayRequest(
        @NotNull
        Long courseImageId,
        @NotBlank(message = "산책로 제목을 입력해주세요.")
        @Size(max = 15, message = "산책로 제목은 15자 이내여야 합니다.")
        String name,
        @Size(max = 100, message = "산책로 내용은 100자 이내여야 합니다.")
        String memo,
        @DecimalMin("0.2")
        Double distance,
        @Min(300)
        Integer time,
        @NotNull
        List<String> hashtags,
        WalkwayExposeLevel exposeLevel,
        List<WalkwayCoordinate> course
) {

    public CreateWalkwayCommand toCreateWalkwayCommand(String imageUrl, Long memberId) {
        return new CreateWalkwayCommand(
                name,
                memo,
                distance,
                time,
                hashtags,
                exposeLevel,
                course,
                imageUrl,
                memberId
        );
    }

}
