package com.dongsan.api.domains.walkway.mapper;

import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class WalkwayMapper {
    private WalkwayMapper(){}

    public static CreateWalkway toCreateWalkway(CreateWalkwayRequest createWalkwayRequest, Image image, Long memberId) {

        LineString course = LineStringMapper.toLineString(createWalkwayRequest.course());
        Point startLocation = course.getStartPoint();
        Point endLocation = course.getEndPoint();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        return new CreateWalkway(
                createWalkwayRequest.name(),
                createWalkwayRequest.distance(),
                createWalkwayRequest.time(),
                createWalkwayRequest.exposeLevel(),
                startLocation,
                endLocation,
                createWalkwayRequest.memo(),
                course,
                image.url(),
                createWalkwayRequest.hashtags(),
                memberId
        );
    }

    public static UpdateWalkway toUpdateWalkway(UpdateWalkwayRequest updateWalkwayRequest, Long walkwayId) {

        return new UpdateWalkway(
                walkwayId,
                updateWalkwayRequest.name(),
                updateWalkwayRequest.memo(),
                updateWalkwayRequest.exposeLevel(),
                updateWalkwayRequest.hashtags()
        );
    }
}
