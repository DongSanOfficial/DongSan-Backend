package com.dongsan.rdb.domains.walkway;

import static fixture.WalkwayEntityFixture.createWalkway;
import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.member.MemberEntity;
import fixture.MemberEntityFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("WalkwayQueryDSLRepositoryTest Unit Test")
class WalkwayQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @Nested
    @DisplayName("getUserWalkway 메소드는")
    class Describe_getUserWalkwayEntity {

        @BeforeEach
        void setUp(){
            MemberEntity memberEntity = MemberEntityFixture.createMember();
            em.persist(memberEntity);
            for(int i=0; i<5; i++){
                WalkwayEntity walkwayEntity = createWalkway(memberEntity);
                em.persist(walkwayEntity);
            }
        }

        @Test
        @DisplayName("lastCreateAt가 null이면 가장 최근의 walkway들을 내림차순으로 가져온다.")
        void it_returns_most_recent_walkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            LocalDateTime lastCreateAt = null;

            // when
            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);

            // then
            for (WalkwayEntity walkwayEntity : result) {
                assertThat(walkwayEntity.getMemberEntity().getId()).isEqualTo(memberId);
            }
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("lastCreateAt가 null이 아니면 walkwayId보다 일찍 등록한 walkway를 내림차순으로 가져온다.")
        void it_returns_next_walkways(){
            // given
            Long memberId = 1L;
            Integer size = 5;
            LocalDateTime lastCreateAt = LocalDateTime.now().minusSeconds(1L);

            // when
            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);

            // then
            for (WalkwayEntity walkwayEntity : result) {
                assertThat(walkwayEntity.getMemberEntity()
                        .getId()).isEqualTo(memberId);
                assertThat(walkwayEntity.getCreatedAt()).isBefore(lastCreateAt);
            }
            for(int i=1; i<result.size(); i++){
                LocalDateTime after = result.get(i-1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }

        @Test
        @DisplayName("내가 작성한 산책로가 존재하지 않으면 빈 리스트를 반환한다.")
        void it_returns_empty_list(){
            // given
            Long memberId = 2L;
            Integer size = 5;
            LocalDateTime lastCreateAt = LocalDateTime.now().minusSeconds(1L);

            // when
            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreateAt);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getUserLikedWalkway 메소드는")
    class Describe_getUserLikedWalkway {

        @BeforeEach
        void setUp() {
            MemberEntity memberEntity = MemberEntityFixture.createMember();
            em.persist(memberEntity);

            for (int i = 0; i < 5; i++) {
                WalkwayEntity walkwayEntity = createWalkway(memberEntity);
                em.persist(walkwayEntity);

                LikedWalkwayEntity likedWalkwayEntity = new LikedWalkwayEntity(memberEntity, walkwayEntity);
                em.persist(likedWalkwayEntity);
            }
        }

        @Test
        @DisplayName("lastCreatedAt가 null이면 사용자가 좋아요한 공개 산책로를 최신순으로 가져온다.")
        void it_returns_most_recent_liked_public_walkways() {
            // given
            Long memberId = 1L;
            Integer size = 5;
            LocalDateTime lastCreatedAt = null;

            // when
            List<WalkwayEntity> result = walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);

            // then
            for (WalkwayEntity walkwayEntity : result) {
                assertThat(walkwayEntity.getExposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
            }
            for (int i = 1; i < result.size(); i++) {
                LocalDateTime after = result.get(i - 1).getCreatedAt();
                LocalDateTime prev = result.get(i).getCreatedAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }
    }
}