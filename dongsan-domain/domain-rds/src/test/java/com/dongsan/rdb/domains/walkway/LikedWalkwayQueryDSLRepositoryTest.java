//package com.dongsan.rdb.domains.walkway;
//
//import static fixture.LikedWalkwayFixture.*;
//import static fixture.WalkwayEntityFixture.*;
//import static org.assertj.core.api.Assertions.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.core.domains.walkway.ExposeLevel;
//import com.dongsan.rdb.domains.member.MemberEntity;
//
//import fixture.MemberEntityFixture;
//
//@DisplayName("LikedWalkwayQueryDSLRepository Unit Test")
//class LikedWalkwayQueryDSLRepositoryTest extends RepositoryTest {
//	@Autowired
//	TestEntityManager em;
//	@Autowired
//	LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
//
//	@Nested
//	@DisplayName("getUserLikedWalkway 메서드는")
//	class Describe_getUserLikedWalkwayEntityEntity {
//		MemberEntity memberEntity;
//
//		@BeforeEach
//		void setUp() {
//			memberEntity = MemberEntityFixture.createMember();
//			em.persist(memberEntity);
//			for (int i = 0; i < 5; i++) {
//				WalkwayEntity walkwayEntity = createWalkway(memberEntity);
//				LikedWalkwayEntity likedWalkway = createLikedWalkway(memberEntity, walkwayEntity);
//				em.persist(walkwayEntity);
//				em.persist(likedWalkway);
//			}
//		}
//
//		@Test
//		@DisplayName("타인이 등록한 산책로의 경우 공개 상태의 산책로만 조회한다.")
//		void it_returns_others_public_walkway() {
//			// given
//			MemberEntity other = MemberEntityFixture.createMember();
//			WalkwayEntity otherPublicWalkwayEntity = createWalkway(other);
//			WalkwayEntity otherPrivateWalkwayEntity = createPrivateWalkway(other);
//			em.persist(other);
//			em.persist(otherPublicWalkwayEntity);
//			em.persist(otherPrivateWalkwayEntity);
//			em.persist(createLikedWalkway(memberEntity, otherPublicWalkwayEntity));
//			em.persist(createLikedWalkway(memberEntity, otherPrivateWalkwayEntity));
//			Long memberId = memberEntity.getId();
//			Integer size = 10;
//			LocalDateTime lastCreateAt = null;
//
//			// when
//			List<LikedWalkwayEntity> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
//				lastCreateAt);
//
//			// then
//			assertThat(result).hasSize(5 + 1);
//			for (int i = 0; i < result.size(); i++) {
//				WalkwayEntity walkwayEntity = result.get(i)
//					.getWalkwayEntity();
//				// 타인이 등록한 산책로 일 때
//				if (!walkwayEntity.getMember()
//					.getId()
//					.equals(memberId)) {
//					assertThat(walkwayEntity.getExposeLevel()).isEqualTo(ExposeLevel.PUBLIC);
//				}
//			}
//		}
//
//		@Test
//		@DisplayName("lastCreateAt가 null이면 가장 최근의 likedWalkway들을 내림차순으로 가져온다.")
//		void it_returns_most_recent_likeWalkways() {
//			// given
//			Long memberId = memberEntity.getId();
//			Integer size = 5;
//			LocalDateTime lastCreateAt = null;
//
//			// when
//			List<LikedWalkwayEntity> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
//				lastCreateAt);
//
//			// then
//			assertThat(result).hasSize(5);
//			for (int i = 1; i < result.size(); i++) {
//				LocalDateTime after = result.get(i - 1)
//					.getCreatedAt();
//				LocalDateTime prev = result.get(i)
//					.getCreatedAt();
//				assertThat(prev).isBeforeOrEqualTo(after);
//			}
//		}
//
//		@Test
//		@DisplayName("lastCreateAt가 null이 아니면 likedWalkwayId보다 일찍 등록한 likedWalkway를 내림차순으로 가져온다.")
//		void it_returns_next_likeWalkways() {
//			// given
//			Long memberId = memberEntity.getId();
//			Integer size = 5;
//			LocalDateTime lastCreateAt = LocalDateTime.now()
//				.minusSeconds(1L);
//
//			// when
//			List<LikedWalkwayEntity> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
//				lastCreateAt);
//
//			// then
//			for (int i = 0; i < result.size(); i++) {
//				LikedWalkwayEntity likedWalkway = result.get(i);
//				assertThat(likedWalkway.getMemberEntity()
//					.getId()).isEqualTo(memberId);
//				assertThat(likedWalkway.getCreatedAt()).isBefore(lastCreateAt);
//			}
//			for (int i = 1; i < result.size(); i++) {
//				LocalDateTime after = result.get(i - 1)
//					.getCreatedAt();
//				LocalDateTime prev = result.get(i)
//					.getCreatedAt();
//				assertThat(prev).isBeforeOrEqualTo(after);
//			}
//		}
//
//		@Test
//		@DisplayName("member가 좋아요한 LikedWalkway가 존재하지 않으면 빈 리스트를 반환한다.")
//		void it_returns_empty_list_when_member_not_liked() {
//			// given
//			Long memberId = memberEntity.getId() + 10;
//			Integer size = 5;
//			LocalDateTime lastCreateAt = LocalDateTime.now()
//				.minusSeconds(1L);
//
//			// when
//			List<LikedWalkwayEntity> result = likedWalkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
//				lastCreateAt);
//
//			// then
//			assertThat(result).isEmpty();
//		}
//
//	}
//}
