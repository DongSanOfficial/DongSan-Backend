package com.dongsan.domain.domains.bookmark.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BookmarkTest {
    private static final Long MEMBER_ID = 1L;

    @Test
    @DisplayName("북마크 소유자가 아니면 이름을 변경할 수 없다")
    void shouldNotRenameBookmark_whenNotOwner() {
        Bookmark bookmark = new Bookmark("기존 북마크", MEMBER_ID);
        String newName = "이름 변경";
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> bookmark.rename(newName, anotherMemberId))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.NOT_BOOKMARK_OWNER);
    }

    @Test
    @DisplayName("북마크의 이름을 변경할 수 있다")
    void shouldRenameBookmark() {
        Bookmark bookmark = new Bookmark("기존 북마크", MEMBER_ID);
        String newName = "이름 변경";

        bookmark.rename(newName, MEMBER_ID);

        assertThat(bookmark.getName()).isEqualTo(newName);
    }


    @Test
    @DisplayName("유효성 검사 시 소유자가 아니면 예외가 발생한다")
    void shouldThrowException_whenIsNotOwner() {
        Bookmark bookmark = new Bookmark("테스트 북마크", MEMBER_ID);
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> bookmark.validateOwner(anotherMemberId))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.NOT_BOOKMARK_OWNER);
    }

    @Test
    @DisplayName("유효성 검사 시 소유자가 맞으면 예외가 발생하지 않는다")
    void shouldPassValidation_whenIsOwner() {
        Bookmark bookmark = new Bookmark("테스트 북마크", MEMBER_ID);

        bookmark.validateOwner(MEMBER_ID);
    }

    @Test
    @DisplayName("북마크를 생성할 수 있다")
    void shouldCreateBookmark() {
        Bookmark bookmark = new Bookmark("새로운 북마크", MEMBER_ID);

        assertThat(bookmark.getName()).isEqualTo("새로운 북마크");
    }

}
