package com.dongsan.api.domains.crew;

import com.dongsan.api.support.IntegrationTest;
import com.dongsan.domain.domains.crew.domain.Capacity;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
import com.dongsan.domain.domains.crew.service.BCryptPasswordHasher;
import fixture.CrewTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CrewInFoFacadeTest extends IntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CrewInFoFacadeTest.class);

    @Autowired
    CrewInfoFacade crewInfoFacade;
    @Autowired
    BCryptPasswordHasher bCryptPasswordHasher;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CrewMemberRepository crewMemberRepository;
    
    @Test
    void joinLimitedCrew_concurrentTest() throws InterruptedException {
        // given
        String password = "123456789";
        String hashedPassword = bCryptPasswordHasher.hash(password);
        int memberLimit = 2;
        Crew crew = new CrewTestBuilder()
                .privateCrew(hashedPassword)
                .capacity(new Capacity(true, memberLimit))
                .build();
        crewRepository.save(crew);
        Long crewId = crew.getId();

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            Long memberId = (long) (i + 1);
            executorService.submit(() -> {
                try {
                    crewInfoFacade.joinCrew(crewId, memberId, password);
                } catch (Exception e) {
                    log.error("가입 실패:( Thread: {}, Exception : {}",
                            Thread.currentThread().getName(),
                            e.getClass().getSimpleName());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        int count = crewMemberRepository.countByCrewId(crewId);
        Assertions.assertEquals(memberLimit, count);
    }

}
