//package com.dongsan.rdb.domains.member;
//
//import static fixture.MemberEntityFixture.*;
//import static org.assertj.core.api.Assertions.*;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.core.domains.auth.Provider;
//import com.dongsan.core.domains.member.Member;
//import com.dongsan.core.domains.member.MemberRole;
//
//@DisplayName("MemberCoreRepository Unit Test")
//@Import(MemberCoreRepository.class)
//class MemberCoreRepositoryTest extends RepositoryTest {
//	@Autowired
//	TestEntityManager em;
//	@Autowired
//	MemberCoreRepository memberCoreRepository;
//
//	@Nested
//	@DisplayName("findById 메서드는")
//	class Describe_findById {
//		@Test
//		void memberId가_존재하면_Member를_반환한다() {
//			// given
//			MemberEntity member = createMember();
//			em.persist(member);
//			Long existMemberId = member.getId();
//
//			// when
//			Optional<Member> result = memberCoreRepository.findById(existMemberId);
//
//			// then
//			assertThat(result).isPresent();
//			assertThat(result.get()
//				.id()).isEqualTo(existMemberId);
//		}
//
//		@Test
//		void memberId가_존재하지_않으면_빈_Optional을_반환한다() {
//			// given
//			MemberEntity member = createMember();
//			em.persist(member);
//			Long notExistMemberId = 100L;
//
//			// when
//			Optional<Member> result = memberCoreRepository.findById(notExistMemberId);
//
//			// then
//			assertThat(result).isEmpty();
//		}
//	}
//
//	@Nested
//	@DisplayName("findByEmail 메서드는")
//	class Describe_findByEmail {
//		@Test
//		void email이_존재하면_Member를_반환한다() {
//			// given
//			MemberEntity member = createMember();
//			em.persist(member);
//			String existEmail = member.getEmail();
//
//			// when
//			Optional<Member> result = memberCoreRepository.findByEmail(existEmail);
//
//			// then
//			assertThat(result).isPresent();
//			assertThat(result.get()
//				.email()).isEqualTo(existEmail);
//		}
//
//		@Test
//		void email이_존재하지_않으면_빈_Optional을_반환한다() {
//			// given
//			MemberEntity member = createMember();
//			em.persist(member);
//			String notExistEmail = "notExistEmail@gmail.com";
//
//			// when
//			Optional<Member> result = memberCoreRepository.findByEmail(notExistEmail);
//
//			// then
//			assertThat(result).isEmpty();
//		}
//	}
//
//	@Nested
//	@DisplayName("save 메서드는")
//	class Describe_save {
//		@Test
//		void Member를_저장한다() {
//			// given
//			String email = "dongsan@gmail.com";
//			String nickname = "동산최고!";
//			String profileImageUrl = "dongsan.png";
//			MemberEntity member = createMember(email, nickname, profileImageUrl);
//			Provider provider = Provider.KAKAO;
//			em.persist(member);
//
//			// when
//			Member result = memberCoreRepository.save(email, nickname, profileImageUrl, MemberRole.ROLE_USER,
//				provider);
//
//			// then
//			assertThat(result).isNotNull();
//			assertThat(result.email()).isEqualTo(email);
//			assertThat(result.nickname()).isEqualTo(nickname);
//			assertThat(result.profileImageUrl()).isEqualTo(profileImageUrl);
//		}
//
//	}
//
//}
