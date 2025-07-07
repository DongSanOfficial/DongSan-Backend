package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CowalkPostTest {

    @Test
    @DisplayName("participantCount(참가인원)이 capacity 이상이면 예외가 발생한다")
    void Throw_Exception_Capacity_Limit() {
        // given
        Integer participantCount = 5;
        Integer capacity = 5;
        Long crewId = 1L;
        Long memberId = 1L;
        String memo = "memo";

        CreateCowalkPostCommand command
                = new CreateCowalkPostCommand(crewId, memberId, LocalDateTime.now(), LocalDateTime.now().plusHours(2), capacity, memo);

        CowalkPost cowalkPost = new CowalkPost(command);

        // when & then
        assertThatThrownBy(() -> cowalkPost.validCapacity(participantCount))
                .isInstanceOf(CoreException.class);
    }

}
