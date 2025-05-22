package com.dongsan.api.domains.walkway.dto.request;

import com.dongsan.rdb.domains.walkway.CreateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.WalkwayCoordinate;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateWalkwayRequest(
        @NotNull
        Long courseImageId,
        @NotBlank(message = "산책로 제목을 입력해주세요.")
        String name,
        String memo,
        @DecimalMin("0.2")
        Double distance,
        @Min(600)
        Integer time,
        @NotNull
        List<String> hashtags,
        ExposeLevel exposeLevel,
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
