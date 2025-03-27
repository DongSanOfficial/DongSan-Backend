package bookmark;

import java.time.LocalDateTime;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.support.util.Author;

public class BookmarkFixture {
	private static final Long BOOKMARK_ID = 1L;
	private static final String TITLE = "북마크1";
	private static final Author AUTHOR = new Author(1L);
	private static final LocalDateTime CREATED_AT = LocalDateTime.now();

	public static Bookmark createBookmark() {
		return new Bookmark(BOOKMARK_ID, TITLE, AUTHOR, CREATED_AT);
	}

	public static Bookmark createBookmark(Long authorId) {
		return new Bookmark(BOOKMARK_ID, TITLE, new Author(authorId), CREATED_AT);
	}

	public static Bookmark createBookmark(Long bookmarkId, String title) {
		return new Bookmark(bookmarkId, title, AUTHOR, CREATED_AT);
	}

	public static BookmarkWithMarkedStatus createBookmarkWithMarkedStatus() {
		return new BookmarkWithMarkedStatus(BOOKMARK_ID, TITLE, CREATED_AT, true);
	}

	public static BookmarkWithMarkedStatus createBookmarkWithMarkedStatus(Long bookmarkId, String title,
		boolean marked) {
		return new BookmarkWithMarkedStatus(bookmarkId, title, CREATED_AT, marked);
	}
}
