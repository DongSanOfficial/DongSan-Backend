package bookmark;

import com.dongsan.core.domains.bookmark.MarkedWalkway;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MarkedWalkwayFixture {
    private static final Long WALKWAY_ID = 1L;
    private static final String NAME = "산책로1";
    private static final LocalDateTime INCLUDED_AT = LocalDateTime.now();
    private static final Double DISTANCE = 2.2;
    private static final List<String> HASHTAGS = new ArrayList<>(List.of("조용한", "한적한"));
    private static final String COURSE_IMAGE_URL = "https://example.com/image1.jpg";

    public static MarkedWalkway createMarkedWalkway(){
        return new MarkedWalkway(WALKWAY_ID, NAME, INCLUDED_AT, DISTANCE, HASHTAGS, COURSE_IMAGE_URL);
    }

    public static MarkedWalkway createMarkedWalkway(Long walkwayId){
        return new MarkedWalkway(walkwayId, NAME, INCLUDED_AT, DISTANCE, HASHTAGS, COURSE_IMAGE_URL);
    }
}
