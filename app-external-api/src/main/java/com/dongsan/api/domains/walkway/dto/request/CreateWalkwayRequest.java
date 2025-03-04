package com.dongsan.api.domains.walkway.dto.request;

import com.dongsan.api.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.api.domains.walkway.LineStringMapper;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.ExposeLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

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
        public CreateWalkway toCreateWalkway(Image image, Long memberId) {

                LineString course = LineStringMapper.toLineString(this.course());
                Point startLocation = course.getStartPoint();
                Point endLocation = course.getEndPoint();

                course.setSRID(4326);
                startLocation.setSRID(4326);
                endLocation.setSRID(4326);

                return new CreateWalkway(
                        this.name(),
                        this.distance(),
                        this.time(),
                        this.exposeLevel(),
                        startLocation,
                        endLocation,
                        this.memo(),
                        course,
                        image.url(),
                        this.hashtags(),
                        memberId
                );
        }
}
