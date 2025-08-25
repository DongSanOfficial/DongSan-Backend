package com.dongsan.domain.domains.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    @Test
    @DisplayName("회원의 닉네임을 변경할 수 있다")
    void shouldChangeMemberNickname() {
        Member member = new Member(1L, "test@example.com", "기존닉네임", "url", MemberRole.ROLE_USER);
        String newNickname = "새로운닉네임";

        member.changeNickname(newNickname);

        assertThat(member.getNickname()).isEqualTo(newNickname);
    }

}
