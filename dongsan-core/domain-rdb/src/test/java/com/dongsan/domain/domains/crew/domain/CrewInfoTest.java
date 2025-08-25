package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreException;
import fixture.CrewInfoTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CrewInfoTest {
    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void shouldThrowException_whenNameIsNull() {
        assertThatThrownBy(() -> new CrewInfoTestBuilder()
                .name(null)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("이름이 공백이면 예외가 발생한다")
    void shouldThrowException_whenNameIsBlank() {
        assertThatThrownBy(() -> new CrewInfoTestBuilder()
                .name("   ")
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("이름이 20자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenNameGt20() {
        String longName = "a".repeat(21);
        assertThatThrownBy(() -> new CrewInfoTestBuilder()
                .name(longName)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("설명이 250자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenDescriptionGt250() {
        String longDesc = "d".repeat(251);
        assertThatThrownBy(() -> new CrewInfoTestBuilder()
                .description(longDesc)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("규칙이 250자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenRuleGt250() {
        String longRule = "r".repeat(251);
        assertThatThrownBy(() -> new CrewInfoTestBuilder()
                .rule(longRule)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("모든 필드가 유효하면 객체 생성에 성공한다")
    void shouldCreateCrew_whenAllFieldsAreValid() {
        CrewInfo info = new CrewInfoTestBuilder().build();
        assertThat(info).isNotNull();
    }

    @Test
    @DisplayName("CrewInfo를 생성한다")
    void shouldCreateCrewInfo_withValidValues() {
        // given
        String name = "유효한 크루명";
        String description = "유효한 설명";
        String rule = "유효한 규칙";
        String imageUrl = "http://example.com/image.jpg";

        // when
        CrewInfo crewInfo = new CrewInfo(name, description, rule, imageUrl);

        // then
        assertThat(crewInfo.getName()).isEqualTo(name);
        assertThat(crewInfo.getDescription()).isEqualTo(description);
        assertThat(crewInfo.getRule()).isEqualTo(rule);
        assertThat(crewInfo.getCrewImageUrl()).isEqualTo(imageUrl);
    }

}
