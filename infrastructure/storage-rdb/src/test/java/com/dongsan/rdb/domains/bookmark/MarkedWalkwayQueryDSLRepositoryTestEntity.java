//package com.dongsan.rdb.domains.bookmark;
//
//import static fixture.BookmarkEntityFixture.createBookmark;
//import static fixture.MarkedWalkwayFixture.createMarkedWalkway;
//import static fixture.MemberEntityFixture.createMember;
//import static fixture.WalkwayEntityFixture.createPrivateWalkway;
//import static fixture.WalkwayEntityFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.core.domains.bookmark.Bookmark;
//import com.dongsan.core.domains.walkway.Walkway;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//@DisplayName("MarkedWalkwayQueryDSLRepository Unit Test")
//class MarkedWalkwayQueryDSLRepositoryTestEntity extends RepositoryTest {
//    @Autowired
//    private TestEntityManager em;
//
//    @Autowired
//    private MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getBookmarkWalkway 메서드는")
//    class Describe_getBookmarkWalkwayEntityEntity {
//        MemberEntity memberEntity;
//        BookmarkEntity bookmarkEntity;
//
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            em.persist(memberEntity);
//            bookmarkEntity = createBookmark(memberEntity);
//            em.persist(bookmarkEntity);
//            for(int i=0; i<5; i++){
//                WalkwayEntity walkwayEntity = createWalkway(memberEntity);
//                MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkwayEntity, bookmarkEntity);
//                em.persist(walkwayEntity);
//                em.persist(markedWalkway);
//            }
//        }
//
//        @Test
//        @DisplayName("타인이 등록한 산책로의 경우 공개 상태의 산책로만 조회한다.")
//        void it_returns_others_public_walkway(){
//            // given
//            MemberEntity other = createMember();
//            WalkwayEntity otherPublicWalkwayEntity = createWalkway(other);
//            WalkwayEntity otherPrivateWalkwayEntity = createPrivateWalkway(other);
//            em.persist(other);
//            em.persist(otherPublicWalkwayEntity);
//            em.persist(otherPrivateWalkwayEntity);
//            em.persist(createMarkedWalkway(otherPrivateWalkwayEntity, bookmarkEntity));
//            em.persist(createMarkedWalkway(otherPublicWalkwayEntity, bookmarkEntity));
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 10;
//            LocalDateTime lastCreatedAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkwayEntity> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            assertThat(result).hasSize(5 + 1);
//            for(int i=0; i<result.size(); i++){
//                WalkwayEntity walkwayEntity = result.get(i).getWalkwayEntity();
//                // 타인이 등록한 산책로 일 때
//                if(!walkwayEntity.getMemberEntity().getId().equals(memberId)){
//                    assertThat(walkwayEntity.getExposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
//                }
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreatedAt이 null이 아니면 lastCreatedAt보다 작은 createdAt의 markedWalkway의 walkway를 가지고 온다.")
//        void it_returns_less_createdAt_markedWalkway(){
//            // given
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 5;
//            LocalDateTime lastCreatedAt = LocalDateTime.now().minusSeconds(1L);
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkwayEntity> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            for(int i=0; i<result.size(); i++){
//                MarkedWalkwayEntity markedWalkway = result.get(i);
//                assertThat(markedWalkway.getBookmarkEntity().getId()).isEqualTo(bookmarkId);
//                assertThat(markedWalkway.getCreatedAt()).isBefore(lastCreatedAt);
//            }
//            for(int i=0; i<result.size()-1; i++){
//                MarkedWalkwayEntity after = result.get(i);
//                MarkedWalkwayEntity prev = result.get(i+1);
//                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreatedAt이 null이면 첫 페이지를 가지고 온다.")
//        void it_returns_first_page(){
//            // given
//            Long bookmarkId = bookmarkEntity.getId();
//            Integer size = 3;
//            LocalDateTime lastCreatedAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<MarkedWalkwayEntity> result = markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmarkId, size, lastCreatedAt,
//                    memberId);
//
//            // then
//            assertThat(result).hasSize(size);
//            for(int i=0; i<result.size(); i++){
//                MarkedWalkwayEntity markedWalkway = result.get(i);
//                assertThat(markedWalkway.getBookmarkEntity().getId()).isEqualTo(bookmarkId);
//            }
//            for(int i=0; i<result.size()-1; i++){
//                MarkedWalkwayEntity after = result.get(i);
//                MarkedWalkwayEntity prev = result.get(i+1);
//                assertThat(after.getCreatedAt()).isAfterOrEqualTo(prev.getCreatedAt());
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("existsMarkedWalkwayByMemberAndWalkway 메서드는")
//    class Describe_existsMarkedWalkwayByMemberAndWalkway {
//        Member member;
//        Bookmark bookmark;
//        Walkway walkway;
//
//        @BeforeEach
//        void setUp() {
//            member = createMember();
//            em.persist(member);
//
//            bookmark = createBookmark(member);
//            em.persist(bookmark);
//
//            walkway = createWalkway(member);
//            em.persist(walkway);
//
//            MarkedWalkwayEntity markedWalkway = createMarkedWalkway(walkway, bookmark);
//            em.persist(markedWalkway);
//        }
//
//        @Test
//        @DisplayName("북마크된 산책로가 존재하면 true를 반환한다.")
//        void it_returns_true_if_marked_walkway_exists() {
//            // given
//            Long walkwayId = walkway.getId();
//            Long memberId = member.getId();
//
//            // when
//            boolean result = markedWalkwayQueryDSLRepository.existsMarkedWalkwayByMemberAndWalkway(walkwayId, memberId);
//
//            // then
//            assertThat(result).isTrue();
//        }
//
//        @Test
//        @DisplayName("북마크되지 않은 산책로인 경우 false를 반환한다.")
//        void it_returns_false_if_marked_walkway_does_not_exist() {
//            // given
//            Long nonExistingWalkwayId = -1L;
//            Long memberId = member.getId();
//
//            // when
//            boolean result = markedWalkwayQueryDSLRepository.existsMarkedWalkwayByMemberAndWalkway(nonExistingWalkwayId, memberId);
//
//            // then
//            assertThat(result).isFalse();
//        }
//    }
//}