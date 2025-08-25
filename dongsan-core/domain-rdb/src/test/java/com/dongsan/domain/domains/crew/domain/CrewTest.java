package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import fixture.CrewTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CrewTest {
    @ParameterizedTest
    @CsvSource(value = {"10, 10", "10, 11"})
    @DisplayName("크루 인원 수가 정원을 초과하면 예외가 발생한다")
    void shouldThrowException_whenCrewIsFull(int memberLimit, int memberCount) {
        Crew crew = new CrewTestBuilder().capacity(new Capacity(true, memberLimit)).build();

        assertThatThrownBy(() -> crew.validateNotFull(memberCount))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.CREW_MEMBER_FULL);
    }


    @ParameterizedTest
    @CsvSource(value = {"10, 9", "10, 1"})
    @DisplayName("크루 인원 수가 정원을 초과하지 않으면 예외가 발생하지 않는다")
    void shouldNotThrowException_whenCrewIsNotFull(int memberLimit, int memberCount) {
        Crew crew = new CrewTestBuilder().capacity(new Capacity(true, memberLimit)).build();

        assertThatNoException().isThrownBy(() -> crew.validateNotFull(memberCount));
    }


    @Test
    @DisplayName("크루가 정원 제한이 있으면 true를 반환한다")
    void shouldReturnTrue_whenCrewIsLimited() {
        Crew limitedCrew = new CrewTestBuilder().capacity(new Capacity(true, 10)).build();
        Crew unlimitedCrew = new CrewTestBuilder().capacity(new Capacity(false, null)).build();

        assertThat(limitedCrew.isLimitedCrew()).isTrue();
        assertThat(unlimitedCrew.isLimitedCrew()).isFalse();
    }

    @Test
    @DisplayName("비밀번호가 있는 크루의 경우 비밀번호가 필요하다")
    void shouldNeedPassword_whenCrewIsPrivate() {
        Crew publicCrew = new CrewTestBuilder().publicCrew().build();
        Crew privateCrew = new CrewTestBuilder().privateCrew("hashed_password").build();

        assertThat(publicCrew.needsPassword()).isFalse();
        assertThat(privateCrew.needsPassword()).isTrue();
    }

    @Test
    @DisplayName("크루를 업데이트할 수 있다")
    void shouldUpdateCrew() {
        Crew crew = new CrewTestBuilder().build();
        CrewInfo newInfo = new CrewInfo("updated name", "updated desc", "updated rule", "updated image");
        Capacity newCapacity = new Capacity(true, 50);
        CrewAccessPolicy newPolicy = CrewAccessPolicy.privateCrew("new_password");

        crew.update(newInfo, newCapacity, newPolicy);

        assertThat(crew.getName()).isEqualTo("updated name");
        assertThat(crew.getDescription()).isEqualTo("updated desc");
        assertThat(crew.getMemberLimit()).isEqualTo(50);
        assertThat(crew.getPassword()).isEqualTo("new_password");
    }


}
