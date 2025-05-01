//package com.dongsan.core.domains.member;
//
//import com.dongsan.core.domains.auth.Provider;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static com.dongsan.core.domains.member.MemberRole.*;
//import static member.MemberFixture.createMember;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("MemberWriter Unit Test")
//class MemberWriterTest {
//    @InjectMocks
//    MemberWriter memberWriter;
//    @Mock
//    MemberRepository memberRepository;
//
//    @Nested
//    @DisplayName("save 메서드는")
//    class Describe_save {
//        @Test
//        void Member를_저장한다() {
//            // given
//            String email = "dongsan@gmail.com";
//            String nickname = "haha";
//            String profileImageUrl = "dongsan.png";
//            MemberRole role = ROLE_USER;
//            Provider provider = Provider.KAKAO;
//            Member member = createMember(email, nickname, profileImageUrl, role);
//            when(memberRepository.save(email, nickname, profileImageUrl, role, provider)).thenReturn(member);
//
//            // when
//            Member result = memberWriter.save(email, nickname, profileImageUrl, role, provider);
//
//            // then
//            assertThat(result).isEqualTo(member);
//        }
//    }
//
//}
