package com.dongsan.api.support.factory;

import java.util.List;

import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.walkway.LineStringMapper;
import com.dongsan.domain.domains.walkway.WalkwayCoordinate;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLiked;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLikedRepository;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRating;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRatingRepository;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;
import com.dongsan.domain.domains.walkway.domain.WalkwayGeometry;
import com.dongsan.domain.domains.walkway.domain.WalkwayInfo;
import com.dongsan.domain.domains.walkway.domain.WalkwayRepository;

@Component
public class WalkwayFactory {
    LineString DEFAULT_COURSE = LineStringMapper.toLineString(
            List.of(
                    new WalkwayCoordinate(37.5000, 127.0000),
                    new WalkwayCoordinate(37.5010, 127.0010),
                    new WalkwayCoordinate(37.5020, 127.0020)
            )
    );
    String DEFAULT_NAME = "test Walkway";
    String DEFAULT_MEMO = "test memo";
    String DEFAULT_IMAGE_URL = "test image url";
    Double DEFAULT_DISTANCE = 0.2;
    Integer DEFAULT_TIME = 300;
    List<String> DEFAULT_HASHTAG = List.of("test");
    Long DEFAULT_MEMBER_ID = 1L;


    @Autowired
    private WalkwayRepository walkwayRepository;
    @Autowired
    private MetaWalkwayLikedRepository metaWalkwayLikedRepository;
    @Autowired
    private MetaWalkwayRatingRepository metaWalkwayRatingRepository;

    public Long save() {
        WalkwayGeometry walkwayGeometry = new WalkwayGeometry(DEFAULT_COURSE, DEFAULT_IMAGE_URL);
        WalkwayInfo walkwayInfo = new WalkwayInfo(DEFAULT_NAME, DEFAULT_DISTANCE, DEFAULT_TIME, WalkwayExposeLevel.PUBLIC, DEFAULT_MEMO, DEFAULT_HASHTAG);
        Walkway walkway = new Walkway(DEFAULT_MEMBER_ID, walkwayInfo, walkwayGeometry);
        walkwayRepository.save(walkway);
        metaWalkwayLikedRepository.save(new MetaWalkwayLiked(walkway.getId()));
        metaWalkwayRatingRepository.save(new MetaWalkwayRating(walkway.getId()));
        return walkway.getId();
    }

}
