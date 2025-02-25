//package com.dongsan.domains.bookmark.repository;
//
//import static fixture.BookmarkFixture.createBookmark;
//import static fixture.MemberFixture.createMember;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.bookmark.BookmarksWithMarkedWalkwayDTO;
//import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
//import com.dongsan.rdb.domains.bookmark.MarkedWalkway;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//import fixture.MarkedWalkwayFixture;
//import fixture.WalkwayFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("BookmarkQueryDSLRepository Unit Test")
//class BookmarkEntityQueryDSLRepositoryTest extends RepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private BookmarkQueryDSLRepository bookmarkQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getBookmarks 메서드는")
//    class Describe_getBookmarks {
//
//        MemberEntity memberEntity1;
//        MemberEntity memberEntity2;
//        List<BookmarkEntity> bookmarkEntityList = new ArrayList<>();
//
//        @BeforeEach
//        void setUpData(){
//            memberEntity1 = createMember();
//            memberEntity2 = createMember();
//            entityManager.persist(memberEntity1);
//            entityManager.persist(memberEntity2);
//            for(long i = 0; i < 10; i++) {
//                BookmarkEntity bookmarkEntity = createBookmark(memberEntity1, "test"+i);
//                bookmarkEntityList.add(bookmarkEntity);
//                entityManager.persist(bookmarkEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("bookmarkId에 값이 있으면 bookmarkId보다 낮은 아이디의 북마크를 limit만큼 내림차순으로 반환한다.")
//        void it_returns_after_bookmarkId_bookmarks() {
//            // Given
//            Integer limit = 3;
//            BookmarkEntity lastBookmarkEntity = bookmarkEntityList.get(5);
//            Long memberId = memberEntity1.getId();
//
//            // When
//            List<BookmarkEntity> result = bookmarkQueryDSLRepository.getBookmarks(lastBookmarkEntity, memberId, limit);
//
//            // Then
//            assertThat(result).hasSize(limit);
//            BookmarkEntity beforeBookmarkEntity = lastBookmarkEntity;
//            for(int i = 1; i < limit; i++) {
//                assertThat(result.get(i).getId()).isLessThan(beforeBookmarkEntity.getId());
//                beforeBookmarkEntity = result.get(i);
//            }
//        }
//
//        @Test
//        @DisplayName("bookmarkId에 값이 null이면 첫 페이지의 북마크를 limit만큼 내림차순으로 반환한다.")
//        void it_returns_first_page_bookmarks() {
//            // Given
//            Integer limit = 3;
//            Long memberId = memberEntity1.getId();
//
//            // When
//            List<BookmarkEntity> result = bookmarkQueryDSLRepository.getBookmarks(null, memberId, limit);
//
//            // Then
//            assertThat(result).hasSize(limit);
//            for(int i = 0; i < limit; i++) {
//                if (i < limit - 1) {
//                    assertThat(result.get(i).getId()).isGreaterThan(result.get(i + 1).getId());
//                }
//            }
//        }
//
//        @Test
//        @DisplayName("북마크가 없으면 빈 리스트를 반환한다.")
//        void it_returns_empty_list() {
//            // Given
//            Integer limit = 3;
//            Long memberId = memberEntity2.getId();
//
//            // When
//            List<BookmarkEntity> result = bookmarkQueryDSLRepository.getBookmarks(null, memberId, limit);
//
//            // Then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
//    class Describe_getBookmarksWithMarkedWalkwayEntity {
//
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        @BeforeEach
//        void setUpData(){
//            memberEntity = createMember();
//            entityManager.persist(memberEntity);
//            walkwayEntity = WalkwayFixture.createWalkway(memberEntity);
//            entityManager.persist(walkwayEntity);
//            for(int i = 0; i < 5; i++) {
//                BookmarkEntity bookmarkEntity = createBookmark(memberEntity, "test"+i);
//                entityManager.persist(bookmarkEntity);
//                MarkedWalkway markedWalkway = MarkedWalkwayFixture.createMarkedWalkway(walkwayEntity, bookmarkEntity);
//                entityManager.persist(markedWalkway);
//            }
//            for(int i = 5; i < 10; i++) {
//                BookmarkEntity bookmarkEntity = createBookmark(memberEntity, "test"+i);
//                entityManager.persist(bookmarkEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("회원의 북마크 리스트를 산책로와 함께 반환한다.")
//        void it_returns_bookmarks() {
//            // Given
//            Long memberId = memberEntity.getId();
//            Long walkwayId = walkwayEntity.getId();
//            Integer size = 10;
//
//            // When
//            List<BookmarksWithMarkedWalkwayDTO> result = bookmarkQueryDSLRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, null, size);
//
//            // Then
//            assertThat(result).hasSize(10);
//            for(int i = 0; i < 10; i++) {
//                if (i >= 5) {
//                    assertThat(result.get(i).markedWalkwayId()).isNotNull();
//                } else {
//                    assertThat(result.get(i).markedWalkwayId()).isNull();
//                }
//            }
//        }
//    }
//
//}