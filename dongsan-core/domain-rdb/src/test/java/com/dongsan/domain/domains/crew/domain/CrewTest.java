package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreException;
import fixture.PrivateCrewTestBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CrewTest {
    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void shouldThrowException_whenNameIsNull() {
        assertThatThrownBy(() -> new PrivateCrewTestBuilder()
                .name(null)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("이름이 공백이면 예외가 발생한다")
    void shouldThrowException_whenNameIsBlank() {
        assertThatThrownBy(() -> new PrivateCrewTestBuilder()
                .name("   ")
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("이름이 20자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenNameGt20() {
        String longName = "a".repeat(21);
        assertThatThrownBy(() -> new PrivateCrewTestBuilder()
                .name(longName)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("설명이 250자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenDescriptionGt250() {
        String longDesc = "d".repeat(251);
        assertThatThrownBy(() -> new PrivateCrewTestBuilder()
                .description(longDesc)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("규칙이 250자를 초과하면 예외가 발생한다")
    void shouldThrowException_whenRuleGt250() {
        String longRule = "r".repeat(251);
        assertThatThrownBy(() -> new PrivateCrewTestBuilder()
                .rule(longRule)
                .build()
        ).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("모든 필드가 유효하면 객체 생성에 성공한다")
    void shouldCreateCrew_whenAllFieldsAreValid() {
        PrivateCrew crew = new PrivateCrewTestBuilder().build();
        Assertions.assertThat(crew).isNotNull();
    }

}
