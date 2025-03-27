package fixture;

import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
import com.dongsan.rdb.domains.bookmark.MarkedWalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;

public class MarkedWalkwayEntityFixture {

	public static MarkedWalkwayEntity createMarkedWalkway(WalkwayEntity walkwayEntity, BookmarkEntity bookmarkEntity) {
		return new MarkedWalkwayEntity(bookmarkEntity, walkwayEntity);
	}

}
