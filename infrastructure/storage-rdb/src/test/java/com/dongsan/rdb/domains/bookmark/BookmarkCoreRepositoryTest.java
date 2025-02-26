package com.dongsan.rdb.domains.bookmark;

import static fixture.BookmarkEntityFixture.createBookmark;
import static fixture.MarkedWalkwayEntityFixture.createMarkedWalkway;
import static fixture.MemberEntityFixture.createMember;
import static fixture.WalkwayEntityFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import fixture.WalkwayEntityFixture;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DisplayName("BookmarkCoreRepository Unit Test")
@Import(BookmarkCoreRepository.class)
class BookmarkCoreRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    BookmarkCoreRepository bookmarkCoreRepository;
    @Autowired
    MarkedWalkwayJpaRepository markedWalkwayJpaRepository;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save{
        @Test
        void Bookmark를_저장하면_bookmarkId를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            Long memberId = member.getId();
            String bookmarkName = "북마크";

            // when
            Long result = bookmarkCoreRepository.save(memberId, bookmarkName);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("existsById 메서드는")
    class Describe_existsById {
        @Test
        void bookmarkId가_존재하면_true를_반환한다() {
            // given
            MemberEntity member = createMember();
            em.persist(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(bookmark);
            Long existBookmarkId = bookmark.getId();

            // when
            boolean result = bookmarkCoreRepository.existsById(existBookmarkId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void bookmarkId가_존재하지_않으면_false를_반환한다() {
            // given
            MemberEntity member = createMember();
            em.persist(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(bookmark);
            Long notExistBookmarkId = 1000L;

            // when
            boolean result = bookmarkCoreRepository.existsById(notExistBookmarkId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class Describe_findById{
        @Test
        void bookmarkId가_존재하면_Bookmark를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(bookmark);
            Long existBookmarkId = bookmark.getId();

            // when
            Optional<Bookmark> result = bookmarkCoreRepository.findById(existBookmarkId);

            // then
            assertThat(result).isPresent();
        }

        @Test
        void bookmarkId가_존재하지_않으면_빈_Optional을_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(bookmark);
            Long notExistBookmarkId = 100L;

            // when
            Optional<Bookmark> result = bookmarkCoreRepository.findById(notExistBookmarkId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByMemberIdAndName 메서드는")
    class Describe_existsByMemberIdAndName{
        @Test
        void 동일한_이름의_Bookmark가_존재하면_true를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            Long memberId = member.getId();
            String existName = "북마크1";
            BookmarkEntity bookmark = createBookmark(member, existName);
            em.persist(bookmark);

            // when
            boolean result = bookmarkCoreRepository.existsByMemberIdAndName(memberId, existName);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 동일한_이름의_Bookmark가_존재하지_않으면_false를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            Long memberId = member.getId();
            String existName = "북마크1";
            String notExistName = "북마크222";
            BookmarkEntity bookmark = createBookmark(member, existName);
            em.persist(bookmark);

            // when
            boolean result = bookmarkCoreRepository.existsByMemberIdAndName(memberId, notExistName);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("rename 메서드는")
    class Describe_rename{
        @Test
        void Bookmark의_이름을_변경한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            String preName = "북마크1";
            String changedName = "북마크222";
            BookmarkEntity bookmark = createBookmark(member, preName);
            em.persist(bookmark);
            Long bookmarkId = bookmark.getId();

            // when
            bookmarkCoreRepository.rename(bookmarkId, changedName);

            // then
            Optional<Bookmark> updateBookmark = bookmarkCoreRepository.findById(bookmarkId);
            assertThat(updateBookmark).isPresent();
            assertThat(updateBookmark.get().title()).isEqualTo(changedName);
        }
    }

    @Nested
    @DisplayName("isWalkwayAdded 메서드는")
    class Describe_isWalkwayAdded{
        @Test
        void MarkedWalkway가_존재하면_true를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            WalkwayEntity walkway = createWalkway(member);
            BookmarkEntity bookmark = createBookmark(member);
            MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
            em.persist(walkway);
            em.persist(bookmark);
            em.persist(markedWalkway);

            // when
            boolean result = bookmarkCoreRepository.isWalkwayAdded(bookmark.getId(), walkway.getId());

            // then
            assertThat(result).isTrue();
        }

        @Test
        void MarkedWalkway가_존재하지_않으면_false를_반환한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            WalkwayEntity walkway = createWalkway(member);
            BookmarkEntity bookmark = createBookmark(member);
            MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
            em.persist(walkway);
            em.persist(bookmark);
            em.persist(markedWalkway);
            Long notExistWalkwayId = 100L;

            // when
            boolean result = bookmarkCoreRepository.isWalkwayAdded(bookmark.getId(), notExistWalkwayId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("includeWalkway 메서드는")
    class Describe_includeWalkway{
        @Test
        void 북마크에_산책로를_추가한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            WalkwayEntity walkway = createWalkway(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(walkway);
            em.persist(bookmark);

            // when
            bookmarkCoreRepository.includeWalkway(bookmark.getId(), walkway.getId());

            // then
            boolean isIncluded = bookmarkCoreRepository.isWalkwayAdded(bookmark.getId(), walkway.getId());
            assertThat(isIncluded).isTrue();
        }
    }

    @Nested
    @DisplayName("excludeWalkway 메서드는")
    class Describe_excludeWalkway{
        @Test
        void 북마크에_산책로를_제외한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            WalkwayEntity walkway = createWalkway(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(walkway);
            em.persist(bookmark);

            // when
            bookmarkCoreRepository.excludeWalkway(bookmark.getId(), walkway.getId());

            // then
            boolean isIncluded = bookmarkCoreRepository.isWalkwayAdded(bookmark.getId(), walkway.getId());
            assertThat(isIncluded).isFalse();
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class Describe_deleteById{
        @Test
        void 북마크와_MarkedWalkway를_삭제한다(){
            // given
            MemberEntity member = createMember();
            em.persist(member);
            BookmarkEntity bookmark = createBookmark(member);
            em.persist(bookmark);
            for(int i=0; i<5; i++){
                WalkwayEntity walkway = createWalkway(member);
                em.persist(walkway);
                MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
                em.persist(markedWalkway);
            }

            // when
            bookmarkCoreRepository.deleteById(bookmark.getId());

            // then
            boolean existBookmark = bookmarkCoreRepository.existsById(bookmark.getId());
            assertThat(existBookmark).isFalse();
            int markedWalkways = markedWalkwayJpaRepository.countByBookmarkId(bookmark.getId());
            assertThat(markedWalkways).isZero();
        }
    }

    @Nested
    @DisplayName("getBookmarkWalkways 메서드는")
    class Describe_getBookmarkWalkways {
        MemberEntity member;
        BookmarkEntity bookmarkEntity;

        @BeforeEach
        void setUp(){
            member = createMember();
            em.persist(member);
            bookmarkEntity = createBookmark(member);
            em.persist(bookmarkEntity);
            for(int i=0; i<5; i++){
                WalkwayEntity walkwayEntity = createWalkway(member);
                MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
                em.persist(walkwayEntity);
                em.persist(markedWalkway);
            }
        }

        @Test
        @DisplayName("타인이 등록한 산책로의 경우 공개 상태의 산책로만 조회한다.")
        void it_returns_others_public_walkway(){
            // given
            MemberEntity other = createMember();
            WalkwayEntity otherPublicWalkway = createWalkway(other);
            WalkwayEntity otherPrivateWalkway = WalkwayEntityFixture.createPrivateWalkway(other);
            em.persist(other);
            em.persist(otherPublicWalkway);
            em.persist(otherPrivateWalkway);
            em.persist(createMarkedWalkway(otherPrivateWalkway, bookmarkEntity));
            em.persist(createMarkedWalkway(otherPublicWalkway, bookmarkEntity));
            Long bookmarkId = bookmarkEntity.getId();
            Integer size = 10;
            LocalDateTime lastCreatedAt = null;
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = bookmarkCoreRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt, memberId);

            // then
            assertThat(result).hasSize(5 + 1);
            for(int i=0; i<result.size(); i++){
                MarkedWalkway walkway = result.get(i);
                // 타인이 등록한 산책로 일 때
                if(!walkway.authorId().equals(memberId)){
                    assertThat(walkway.exposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
                }
            }
        }

        @Test
        @DisplayName("lastCreatedAt이 null이 아니면 lastCreatedAt보다 작은 createdAt의 markedWalkway의 walkway를 가지고 온다.")
        void it_returns_less_createdAt_markedWalkway(){
            // given
            Long bookmarkId = bookmarkEntity.getId();
            Integer size = 5;
            LocalDateTime lastCreatedAt = LocalDateTime.now().minusSeconds(1L);
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = bookmarkCoreRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt, memberId);

            // then
            for(int i=0; i<result.size(); i++){
                MarkedWalkway walkway = result.get(i);
                assertThat(walkway.includedAt()).isBefore(lastCreatedAt);
            }
            for(int i=0; i<result.size()-1; i++){
                MarkedWalkway after = result.get(i);
                MarkedWalkway prev = result.get(i+1);
                assertThat(after.includedAt()).isAfterOrEqualTo(prev.includedAt());
            }
        }

        @Test
        @DisplayName("lastCreatedAt이 null이면 첫 페이지를 가지고 온다.")
        void it_returns_first_page(){
            // given
            Long bookmarkId = bookmarkEntity.getId();
            Integer size = 3;
            LocalDateTime lastCreatedAt = null;
            Long memberId = member.getId();

            // when
            List<MarkedWalkway> result = bookmarkCoreRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt,
                    memberId);

            // then
            assertThat(result).hasSize(size);
            for(int i=0; i<result.size()-1; i++){
                MarkedWalkway after = result.get(i);
                MarkedWalkway prev = result.get(i+1);
                assertThat(after.includedAt()).isAfterOrEqualTo(prev.includedAt());
            }
        }
    }

    @Nested
    @DisplayName("getUserBookmarks 메서드는")
    class Describe_getUserBookmarks {
        MemberEntity member1;
        MemberEntity member2;
        List<BookmarkEntity> bookmarkEntityList = new ArrayList<>();

        @BeforeEach
        void setUpData(){
            member1 = createMember();
            member2 = createMember();
            em.persist(member1);
            em.persist(member2);
            for(long i = 0; i < 10; i++) {
                BookmarkEntity bookmarkEntity = createBookmark(member1, "test"+i);
                bookmarkEntityList.add(bookmarkEntity);
                em.persist(bookmarkEntity);
            }
        }

        @Test
        @DisplayName("bookmarkId에 값이 있으면 bookmarkId보다 낮은 아이디의 북마크를 limit만큼 내림차순으로 반환한다.")
        void it_returns_after_bookmarkId_bookmarks() {
            // Given
            Integer size = 3;
            BookmarkEntity lastBookmarkEntity = bookmarkEntityList.get(5);
            Long memberId = member1.getId();

            // When
            List<Bookmark> result = bookmarkCoreRepository.getUserBookmarks(size, lastBookmarkEntity.getCreatedAt(), memberId);

            // Then
            assertThat(result)
                    .hasSize(size)
                    .isSortedAccordingTo(Comparator.comparing(Bookmark::createdAt).reversed());
            assertThat(result)
                    .allMatch(bookmark -> bookmark.createdAt().isBefore(lastBookmarkEntity.getCreatedAt()));
            assertThat(result)
                    .extracting(Bookmark::bookmarkId)
                    .doesNotContain(lastBookmarkEntity.getId());
        }

        @Test
        @DisplayName("bookmarkId에 값이 null이면 첫 페이지의 북마크를 limit만큼 내림차순으로 반환한다.")
        void it_returns_first_page_bookmarks() {
            // Given
            Integer size = 3;
            Long memberId = member1.getId();

            // When
            List<Bookmark> result = bookmarkCoreRepository.getUserBookmarks(size, null, memberId);

            // Then
            assertThat(result).hasSize(size);
            for(int i = 0; i < size; i++) {
                if (i < size - 1) {
                    assertThat(result.get(i).bookmarkId()).isGreaterThan(result.get(i + 1).bookmarkId());
                }
            }
        }

        @Test
        @DisplayName("북마크가 없으면 빈 리스트를 반환한다.")
        void it_returns_empty_list() {
            // Given
            Integer size = 3;
            Long memberId = member2.getId();

            // When
            List<Bookmark> result = bookmarkCoreRepository.getUserBookmarks(size, null, memberId);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
    class Describe_getBookmarksWithMarkedWalkway {

        MemberEntity member;
        WalkwayEntity walkway;
        @BeforeEach
        void setUpData(){
            member = createMember();
            em.persist(member);
            walkway = createWalkway(member);
            em.persist(walkway);
            for(int i = 0; i < 5; i++) {
                BookmarkEntity bookmarkEntity = createBookmark(member, "test"+i);
                em.persist(bookmarkEntity);
                MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmarkEntity);
                em.persist(markedWalkway);
            }
            for(int i = 5; i < 10; i++) {
                BookmarkEntity bookmarkEntity = createBookmark(member, "test"+i);
                em.persist(bookmarkEntity);
            }
        }

        @Test
        @DisplayName("회원의 북마크 리스트를 산책로와 함께 반환한다.")
        void it_returns_bookmarks() {
            // Given
            Long memberId = member.getId();
            Long walkwayId = walkway.getId();
            Integer size = 10;

            // When
            List<BookmarkWithMarkedStatus> result = bookmarkCoreRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, null, size);

            // Then
            assertThat(result).hasSize(10);
            for(int i = 0; i < 10; i++) {
                if (i >= 5) {
                    assertThat(result.get(i).marked()).isTrue();
                } else {
                    assertThat(result.get(i).marked()).isFalse();
                }
            }
        }
    }

    @Nested
    @DisplayName("existsMarkedWalkway 메서드는")
    class Describe_existsMarkedWalkway{
        MemberEntity member;
        WalkwayEntity walkway;

        @BeforeEach
        void setUpData() {
            member = createMember();
            em.persist(member);
            walkway = createWalkway(member);
            em.persist(walkway);
            for (int i = 0; i < 5; i++) {
                BookmarkEntity bookmark = createBookmark(member, "test" + i);
                em.persist(bookmark);
                MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
                em.persist(markedWalkway);
            }
            for (int i = 5; i < 10; i++) {
                BookmarkEntity bookmark = createBookmark(member, "test" + i);
                em.persist(bookmark);
            }
        }

        @Test
        @DisplayName("산책로와 북마크 리스트에 대한 마크 상태를 반환한다.")
        void it_returns_marked_walkway_status() {
            // Given
            Long walkwayId = walkway.getId();
            List<Long> bookmarkIds = new ArrayList<>();
            for (long i = 1; i <= 10; i++) {
                bookmarkIds.add(i);
            }

            // When
            Map<Long, Boolean> result = bookmarkCoreRepository.existsMarkedWalkway(walkwayId, bookmarkIds);

            // Then
            assertThat(result).hasSize(10);
            for (long i = 1; i <= 10; i++) {
                if (i <= 5) {
                    assertThat(result.get(i)).isTrue();
                } else {
                    assertThat(result.get(i)).isFalse();
                }
            }
        }
    }

    @Nested
    @DisplayName("existsByMemberIdAndWalkwayId 메서드는")
    class Describe_existsByMemberIdAndWalkwayId {
        MemberEntity member;
        WalkwayEntity walkway;

        @BeforeEach
        void setUpData() {
            member = createMember();
            em.persist(member);
            walkway = createWalkway(member);
            em.persist(walkway);
            BookmarkEntity bookmark = createBookmark(member, "testBookmark");
            em.persist(bookmark);
            MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
            em.persist(markedWalkway);
        }

        @Test
        @DisplayName("특정 회원과 특정 산책로에 대한 마킹이 존재하면 true를 반환한다.")
        void it_returns_true_when_mark_exists() {
            // Given
            Long memberId = member.getId();
            Long walkwayId = walkway.getId();

            // When
            boolean result = bookmarkCoreRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("특정 회원과 특정 산책로에 대한 마킹이 존재하지 않으면 false를 반환한다.")
        void it_returns_false_when_mark_does_not_exist() {
            // Given
            Long notExistMemberId = 999L;
            Long walkwayId = walkway.getId();

            // When
            boolean result = bookmarkCoreRepository.existsByMemberIdAndWalkwayId(notExistMemberId, walkwayId);

            // Then
            assertThat(result).isFalse();
        }
    }




}
