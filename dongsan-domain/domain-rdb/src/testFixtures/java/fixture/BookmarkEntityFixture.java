package fixture;

import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rds.domains.member.Member;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class BookmarkEntityFixture {

    private static final String NAME = "산책로 모음이에용";

    public static BookmarkEntity createBookmark(Member member) {
        return new BookmarkEntity(NAME, member);
    }

    public static BookmarkEntity createBookmark(Member member, String name) {
        return new BookmarkEntity(name, member);
    }

    public static BookmarkEntity createBookmarkWithId(Long id, Member member) {
        BookmarkEntity bookmarkEntity = createBookmark(member);
        reflectId(id, bookmarkEntity);
        reflectCreatedAt(LocalDateTime.now(), bookmarkEntity);
        return bookmarkEntity;
    }

    public static BookmarkEntity createBookmarkWithId(Long id, Member member, String name) {
        BookmarkEntity bookmarkEntity = createBookmark(member, name);
        reflectId(id, bookmarkEntity);
        reflectCreatedAt(LocalDateTime.now(), bookmarkEntity);
        return bookmarkEntity;
    }

    private static void reflectId(Long id, BookmarkEntity bookmarkEntity) {
        try {
            Field idField = BookmarkEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bookmarkEntity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, BookmarkEntity bookmarkEntity) {
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(bookmarkEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
