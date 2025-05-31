package com.dongsan.api.domains.cowalk;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dongsan.domain.domains.cowalk.service.CowalkParticipantRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkPostRdbService;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;

@ExtendWith(MockitoExtension.class)
class CowalkPostFacadeTest {

	@Mock
	private CrewMemberRdbService crewMemberRdbService;

	@Mock
	private CowalkParticipantRdbService cowalkParticipantRdbService;

	@Mock
	private CowalkPostRdbService cowalkPostRdbService;

	@InjectMocks
	private CowalkPostFacade cowalkPostFacade;

	@Test
	void 동시에_100명이_참여요청_하면_정원까지만_저장된다() throws InterruptedException {
		// given
		int executeCount = 100;
		int maxParticipants = 30;
		ExecutorService executor = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(executeCount);
		AtomicInteger participantCounter = new AtomicInteger();

		// 크루 멤버 검증은 항상 통과
		doNothing().when(crewMemberRdbService).validateIsCrewMember(anyLong(), anyLong());

		// 참가자 수: 호출될 때마다 증가하는 방식으로 mock
		when(cowalkParticipantRdbService.countByCowalkPostId(anyLong()))
			.thenAnswer(invocation -> participantCounter.get());

		// 정원 검증: 30명 이상이면 예외 발생
		doAnswer(invocation -> {
			Long cowalkPostId = invocation.getArgument(0);
			int count = invocation.getArgument(1);
			if (count >= maxParticipants) {
				throw new IllegalStateException("정원이 초과되었습니다.");
			}
			return null;
		}).when(cowalkPostRdbService).validJoin(anyLong(), anyInt());

		// save(): 성공 시에만 호출되고, 호출될 때마다 카운터 증가
		when(cowalkParticipantRdbService.save(anyLong(), anyLong()))
			.thenAnswer(invocation -> {
				participantCounter.incrementAndGet();
				return 1L;
			});

		// when
		for (int i = 0; i < executeCount; i++) {
			final long memberId = i + 1;
			executor.execute(() -> {
				try {
					cowalkPostFacade.joinCowalkPost(1L, 1L, memberId);
				} catch (Exception ignored) {
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executor.shutdown();

		// then
		assertThat(participantCounter.get()).isEqualTo(maxParticipants);
		verify(cowalkParticipantRdbService, times(maxParticipants)).save(anyLong(), anyLong());
	}
}