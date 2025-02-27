package com.dongsan.core.domains.member;

import static com.dongsan.core.domains.member.MemberRole.ROLE_USER;
import static member.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberReader Unit Test")
class MemberReaderTest {
    @InjectMocks
    MemberReader memberReader;
    @Mock
    MemberRepository memberRepository;

    @Nested
    @DisplayName("readMember 메서드는")
    class Describe_readMember {
        @Test
        void 사용자가_존재하면_Member를_반환한다() {
            // Given
            Long memberId = 1L;
            Member member = createMember(memberId);
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

            // When
            Member result = memberReader.readMember(memberId);

            // Then
            Assertions.assertThat(result).isEqualTo(member);
        }

        @Test
        void 사용자가_존재하지_않으면_예외를_반환한다() {
            // Given
            Long memberId = 1L;

            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            // When & Then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                memberReader.readMember(memberId);
            });
            assertEquals(CoreErrorCode.MEMBER_NOT_FOUND, thrown.getErrorCode());
        }
    }

    @Nested
    @DisplayName("readOptionalMember 메서드는")
    class Describe_readOptionalMember{
        @Test
        void Member가_존재하면_Member를_반환한다(){
            // given
            Long existMemberId = 1L;
            Member member = createMember(existMemberId);
            when(memberRepository.findById(existMemberId)).thenReturn(Optional.of(member));

            // when
            Optional<Member> result = memberReader.readOptionalMember(existMemberId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(member);
        }

        @Test
        void Member가_존재하지_않으면_빈_Optional을_반환한다(){
            // given
            Long notExistMemberId = 100L;
            when(memberRepository.findById(notExistMemberId)).thenReturn(Optional.empty());

            // when
            Optional<Member> result = memberReader.readOptionalMember(notExistMemberId);

            // then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("readOptionalMemberByEmail")
    class Describe_readOptionalMemberByEmail{
        @Test
        void Member가_존재하면_Member를_반환한다(){
            // given
            String existEmail = "dongsan@gmail.com";
            Member member = createMember(existEmail, "haha", "dongsan.png", ROLE_USER);
            when(memberRepository.findByEmail(existEmail)).thenReturn(Optional.of(member));

            // when
            Optional<Member> result = memberReader.readOptionalMemberByEmail(existEmail);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(member);
        }

        @Test
        void Member가_존재하지_않으면_빈_Optional을_반환한다(){
            // given
            String notExistEmail = "notExistEmail@gmail.com";
            when(memberRepository.findByEmail(notExistEmail)).thenReturn(Optional.empty());

            // when
            Optional<Member> result = memberReader.readOptionalMemberByEmail(notExistEmail);

            // then
            assertThat(result).isEmpty();
        }
    }


}