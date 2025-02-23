//package com.dongsan.domains.bookmark.repository;
//
//import static fixture.BookmarkFixture.createBookmark;
//import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.bookmark.BookmarkEntity;
//import com.dongsan.rdb.domains.bookmark.MarkedWalkway;
//import com.dongsan.rdb.domains.bookmark.MarkedWalkwayJpaRepository;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("MarkedWalkwayRepository Unit Test")
//class MarkedWalkwayJpaRepositoryTestEntity extends RepositoryTest {
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private MarkedWalkwayJpaRepository markedWalkwayJpaRepository;
//
//    @Nested
//    @DisplayName("existsByBookmarkIdAndWalkwayId 메서드는")
//    class Describe_existsByBookmarkIdAndWalkwayIdEntityEntity {
//        @BeforeEach
//        void setUp(){
//            BookmarkEntity bookmarkEntity = createBookmark(null);
//            WalkwayEntity walkwayEntity = createWalkway(null);
//            MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
//            em.persist(bookmarkEntity);
//            em.persist(walkwayEntity);
//            em.persist(markedWalkway);
//        }
//
//        @Test
//        @DisplayName("북마크에 산책로가 존재하면 true를 반환한다.")
//        void it_returns_true(){
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 1L;
//
//            // when
//            boolean result = markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
//
//            // then
//            assertThat(result).isTrue();
//        }
//
//        @Test
//        @DisplayName("북마크에 산책로가 존재하지 않으면 false를 반환한다.")
//        void it_returns_false(){
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 999L;
//
//            // when
//            boolean result = markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
//
//            // then
//            assertThat(result).isFalse();
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteByBookmarkIdAndWalkwayId 메소드는")
//    class Describe_deleteByBookmarkIdAndWalkwayIdEntityEntity {
//        @BeforeEach
//        void setUp(){
//            BookmarkEntity bookmarkEntity = createBookmark(null);
//            WalkwayEntity walkwayEntity = createWalkway(null);
//            MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
//            em.persist(bookmarkEntity);
//            em.persist(walkwayEntity);
//            em.persist(markedWalkway);
//        }
//
//        @Test
//        @DisplayName("markedWalkway를 삭제한다.")
//        void it_deletes_markedWalkway(){
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 1L;
//
//            // when
//            markedWalkwayJpaRepository.deleteByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
//
//            // then
//            boolean result = markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
//            assertThat(result).isFalse();
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteAllByBookmarkId 메서드는")
//    class Describe_deleteAllByBookmarkIdEntity {
//        BookmarkEntity bookmarkEntity1;
//        BookmarkEntity bookmarkEntity2;
//
//        @BeforeEach
//        void setUp(){
//            bookmarkEntity1 = createBookmark(null);
//            em.persist(bookmarkEntity1);
//            for(int i=0; i<5; i++){
//                WalkwayEntity walkwayEntity = createWalkway(null);
//                MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity1);
//                em.persist(walkwayEntity);
//                em.persist(markedWalkway);
//            }
//            bookmarkEntity2 = createBookmark(null);
//            em.persist(bookmarkEntity2);
//            for(int i=0; i<3; i++){
//                WalkwayEntity walkwayEntity = createWalkway(null);
//                MarkedWalkway markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity2);
//                em.persist(walkwayEntity);
//                em.persist(markedWalkway);
//            }
//        }
//
//        @Test
//        @DisplayName("북마크에 해당되는 MarkedWalkway들을 전부 삭제한다.")
//        void it_deletes_all_markedWalkway(){
//            // given
//            Long bookmarkId = bookmarkEntity1.getId();
//
//            // when
//            markedWalkwayJpaRepository.deleteAllByBookmarkId(bookmarkId);
//
//            // then
//            int count = markedWalkwayJpaRepository.countByBookmarkId(bookmarkId);
//            assertThat(count).isZero();
//        }
//    }
//
//}