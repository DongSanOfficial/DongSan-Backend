package bookmark;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.support.util.Author;
import java.time.LocalDateTime;

public class BookmarkFixture {
    private static final Long BOOKMARK_ID = 1L;
    private static final String TITLE = "북마크1";
    private static final Author AUTHOR = new Author(1L);
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    public static Bookmark createBookmark(){
        return new Bookmark(BOOKMARK_ID, TITLE, AUTHOR, CREATED_AT);
    }

    public static Bookmark createBookmark(Long bookmarkId, String title){
        return new Bookmark(bookmarkId, title, AUTHOR, CREATED_AT);
    }
}
