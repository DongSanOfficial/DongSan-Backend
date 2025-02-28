package com.dongsan.core.domains.member;

import static com.dongsan.core.domains.member.MemberRole.ROLE_USER;
import static member.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Unit Test")
class MemberServiceTest {
    @InjectMocks
    MemberService memberService;

    @Mock
    MemberReader memberReader;

    @Mock
    MemberWriter memberWriter;

    @Nested
    @DisplayName("getMember 메서드는")
    class Describe_getMember{
        @Test
        void Member를_반환한다(){
            // given
            Long memberId = 1L;
            Member member = createMember(memberId);
            when(memberReader.readMember(memberId)).thenReturn(member);

            // when
            Member result = memberService.getMember(memberId);

            // then
            assertThat(result).isEqualTo(member);
            assertThat(result.id()).isEqualTo(memberId);
        }
    }

    @Nested
    @DisplayName("getOptionalMemberByEmail 메서드는")
    class Describe_getOptionalMemberByEmail{
        @Test
        void Member가_존재하면_Member를_반환한다(){
            // given
            String existEmail = "dongsan@gmail.com";
            Member member = createMember(existEmail, "haha", "dongsan.png", ROLE_USER);
            when(memberReader.readOptionalMemberByEmail(existEmail)).thenReturn(Optional.of(member));

            // when
            Optional<Member> result = memberService.getOptionalMemberByEmail(existEmail);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(member);
        }

        @Test
        void Member가_존재하지_않으면_빈_Optional을_반환한다(){
            // given
            String notExistEmail = "notExistEmail@gmail.com";
            when(memberReader.readOptionalMemberByEmail(notExistEmail)).thenReturn(Optional.empty());

            // when
            Optional<Member> result = memberService.getOptionalMemberByEmail(notExistEmail);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save{
        @Test
        void Member를_저장한다(){
            // given
            String email = "dongsan@gmail.com";
            String nickname = "haha";
            String profileImageUrl = "dongsan.png";
            MemberRole role = ROLE_USER;
            Member member = createMember(email, nickname, profileImageUrl, role);
            when(memberWriter.save(email, nickname, profileImageUrl, role)).thenReturn(member);

            // when
            Member result = memberService.save(email, nickname, profileImageUrl, role);

            // then
            assertThat(result).isEqualTo(member);
        }
    }
}