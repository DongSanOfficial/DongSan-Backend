package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

class CrewAccessPolicyTest {
    @Test
    @DisplayName("비밀번호가 null이면 예외가 발생한다")
    void shouldThrowException_whenPasswordIsNull() {
        assertThatThrownBy(() -> CrewAccessPolicy.privateCrew(null))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("비밀번호가 빈 문자열이면 예외가 발생한다")
    void shouldThrowException_whenPasswordIsBlank() {
        assertThatThrownBy(() -> CrewAccessPolicy.privateCrew("    "))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("공개 크루(PUBLIC)를 생성할 수 있다")
    void shouldCreatePublicCrewPolicy() {
        CrewAccessPolicy policy = CrewAccessPolicy.publicCrew();

        assertThat(policy.getCrewExposeLevel()).isEqualTo(CrewExposeLevel.PUBLIC);
        assertThat(policy.getHashedPassword()).isNull();
    }

    @Test
    @DisplayName("비공개 크루(PRIVATE)를 생성하려면 비밀번호가 필요하다")
    void shouldCreatePrivateCrewPolicy_withValidPassword() {
        String hashedPassword = "hashedPassword";

        CrewAccessPolicy policy = CrewAccessPolicy.privateCrew(hashedPassword);

        assertThat(policy.getCrewExposeLevel()).isEqualTo(CrewExposeLevel.PRIVATE);
        assertThat(policy.getHashedPassword()).isEqualTo(hashedPassword);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("비공개 크루(PRIVATE) 생성 시, 비밀번호가 공백이면 예외가 발생한다")
    void shouldThrowException_whenInvalidPasswordGivenForPrivateCrew(String invalidPassword) {
        assertThatThrownBy(() -> CrewAccessPolicy.privateCrew(invalidPassword))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.PRIVATE_CREW_PASSWORD_NOT_VALID);
    }

    @Test
    @DisplayName("비공개 크루는 비밀번호가 필요하다")
    void shouldNeedPassword_whenCrewIsPrivate() {
        CrewAccessPolicy publicCrewPolicy = CrewAccessPolicy.publicCrew();
        CrewAccessPolicy privateCrewPolicy = CrewAccessPolicy.privateCrew("password");

        assertThat(publicCrewPolicy.needsPassword()).isFalse();
        assertThat(privateCrewPolicy.needsPassword()).isTrue();
    }

    @Test
    @DisplayName("공개 크루는 멤버 여부와 상관없이 접근할 수 있다")
    void canAccessPublicCrew_regardlessOfMembership() {
        CrewAccessPolicy publicCrewPolicy = CrewAccessPolicy.publicCrew();

        assertThatNoException().isThrownBy(() -> publicCrewPolicy.canAccess(false));
        assertThatNoException().isThrownBy(() -> publicCrewPolicy.canAccess(true));
    }

    @Test
    @DisplayName("비공개 크루는 멤버가 아니면 접근할 수 없다")
    void cannotAccessPrivateCrew_whenIsNotMember() {
        CrewAccessPolicy privateCrewPolicy = CrewAccessPolicy.privateCrew("password");

        assertThatThrownBy(() -> privateCrewPolicy.canAccess(false))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.CREW_CANT_ACCESS);
    }

    @Test
    @DisplayName("비공개 크루는 멤버인 경우 접근할 수 있다")
    void canAccessPrivateCrew_whenIsMember() {
        CrewAccessPolicy privateCrewPolicy = CrewAccessPolicy.privateCrew("password");

        assertThatNoException().isThrownBy(() -> privateCrewPolicy.canAccess(true));
    }

}
