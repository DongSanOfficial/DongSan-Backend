package com.dongsan.domain.domains.crew.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CrewMemberTest {
    @Test
    @DisplayName("새로운 크루 멤버를 생성한다")
    void shouldCreateCrewMember() {
        Long crewId = 1L;
        Long memberId = 10L;
        CrewMemberRole role = CrewMemberRole.MEMBER;

        CrewMember crewMember = new CrewMember(crewId, memberId, role);

        assertThat(crewMember.getCrewId()).isEqualTo(crewId);
        assertThat(crewMember.getMemberId()).isEqualTo(memberId);
    }

    @ParameterizedTest
    @CsvSource(value = {"MANAGER, true", "MEMBER, false"})
    @DisplayName("crew의 MANAGER인지 확인할 수 있다.")
    void shouldReturnCorrectBoolean_whenCheckingIsManager(CrewMemberRole role, boolean expected) {
        CrewMember crewMember = new CrewMember(1L, 1L, role);

        boolean isManager = crewMember.isManager();

        assertThat(isManager).isEqualTo(expected);
    }

}
