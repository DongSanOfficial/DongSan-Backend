package bookmark;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.domains.walkway.ExposeLevel;

public class MarkedWalkwayFixture {
	private static final Long WALKWAY_ID = 1L;
	private static final Long AUTHOR_ID = 2L;
	private static final String NAME = "산책로1";
	private static final LocalDateTime INCLUDED_AT = LocalDateTime.now();
	private static final Double DISTANCE = 2.2;
	private static final List<String> HASHTAGS = new ArrayList<>(List.of("조용한", "한적한"));
	private static final String COURSE_IMAGE_URL = "https://example.com/image1.jpg";
	private static final ExposeLevel EXPOSE_LEVEL = ExposeLevel.PUBLIC;
	private static final int LIKE_COUNT = 10;
	private static final int REVIVE_COUNT = 10;
	private static final double RATING = 3.4;

	public static MarkedWalkway createMarkedWalkway() {
		return new MarkedWalkway(WALKWAY_ID, AUTHOR_ID, NAME, INCLUDED_AT, DISTANCE, HASHTAGS, COURSE_IMAGE_URL,
			EXPOSE_LEVEL, LIKE_COUNT, REVIVE_COUNT, RATING);
	}

	public static MarkedWalkway createMarkedWalkway(Long walkwayId, Long authorId) {
		return new MarkedWalkway(walkwayId, authorId, NAME, INCLUDED_AT, DISTANCE, HASHTAGS, COURSE_IMAGE_URL,
			EXPOSE_LEVEL, LIKE_COUNT, REVIVE_COUNT, RATING);
	}
}
