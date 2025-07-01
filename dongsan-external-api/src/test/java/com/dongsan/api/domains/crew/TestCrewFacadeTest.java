package com.dongsan.api.domains.crew;

import com.dongsan.domain.domains.crew.domain.CrewMemberRepository;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class TestCrewFacadeTest {

    private static final Logger log = LoggerFactory.getLogger(TestCrewFacadeTest.class);

    @Autowired
    TestCrewFacade testCrewFacade;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CrewMemberRepository crewMemberRepository;

//    @BeforeEach
//    void setUp() {
//        Capacity capacity = new Capacity(true, 100);
//        Crew crew = new CrewTestBuilder()
//                .privateCrew("hashed")
//                .capacity(capacity)
//                .build();
//        crewRepository.save(crew);
//    }

    @Test
    void joinLimitedCrew_concurrentTest() throws InterruptedException {
        // given
        Long crewId = 2L;
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            Long memberId = (long) (i + 1);
            executorService.submit(() -> {
                try {
                    testCrewFacade.joinLimitedCrew(crewId, memberId);
                } catch (Exception e) {
                    log.error("예외 발생! Thread: {}, Exception : {}",
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
        Assertions.assertEquals(count, 10);
    }

}
