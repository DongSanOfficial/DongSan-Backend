package com.dongsan.api.domains.walkway.dto.request;

import java.util.List;

import com.dongsan.domain.domains.walkway.CreateWalkwayCommand;
import com.dongsan.domain.domains.walkway.WalkwayCoordinate;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
        WalkwayExposeLevel walkwayExposeLevel,
        List<WalkwayCoordinate> course
) {

    public CreateWalkwayCommand toCreateWalkwayCommand(String imageUrl, Long memberId) {
        return new CreateWalkwayCommand(
                name,
                memo,
                distance,
                time,
                hashtags,
                walkwayExposeLevel,
                course,
                imageUrl,
                memberId
        );
    }

}
