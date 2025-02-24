package com.dongsan.rdb.domains.walkway;

import static org.assertj.core.api.Assertions.assertThat;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.rdb.domains.member.MemberEntity;
import fixture.MemberEntityFixture;
import fixture.WalkwayEntityFixture;
import fixture.WalkwayHistoryFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("WalkwayHistoryQueryDSLRepository Unit Test")
class WalkwayHistoryQueryDSLRepositoryTest extends RepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;

    @Nested
    @DisplayName("getCanReviewWalkwayHistories 메서드는")
    class Describe_getCanReviewWalkwayHistories {
        MemberEntity member;
        WalkwayEntity walkway;
        @BeforeEach
        void setUp() {
            member = MemberEntityFixture.createMember();
            walkway = WalkwayEntityFixture.createWalkway(member);
            em.persist(member);
            em.persist(walkway);
            for (int i = 0; i < 5; i++) {
                WalkwayHistoryEntity history = WalkwayHistoryFixture.createWalkwayHistory(member, walkway, 1.8, 10);
                em.persist(history);
            }
        }

        @Test
        @DisplayName("해당 회원이 산책한 기록 중 리뷰 가능 거리(2/3 이상)이며, 아직 리뷰하지 않은 기록만 반환한다.")
        void it_returns_can_review_walkway_histories() {
            // given
            Long memberId = member.getId();
            Long walkwayId = walkway.getId();

            // when
            List<WalkwayHistoryEntity> result = walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId);

            // then
            assertThat(result).hasSize(5);

            for (WalkwayHistoryEntity history : result) {
                assertThat(history.getDistance()).isGreaterThanOrEqualTo(walkway.getDistance() * (2.0 / 3.0));
                assertThat(history.getIsReviewed()).isFalse();
            }
        }
    }
}