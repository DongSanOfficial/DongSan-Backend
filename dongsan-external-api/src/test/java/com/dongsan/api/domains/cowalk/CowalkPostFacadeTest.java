package com.dongsan.api.domains.cowalk;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dongsan.api.support.IntegrationTest;
import com.dongsan.domain.domains.auth.Provider;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.infrastructure.CowalkParticipantCoreRepository;
import com.dongsan.domain.domains.cowalk.infrastructure.CowalkPostCoreRepository;
import com.dongsan.domain.domains.crew.domain.Capacity;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberRole;
import com.dongsan.domain.domains.crew.domain.PublicCrew;
import com.dongsan.domain.domains.crew.infrastructure.CrewCoreRepository;
import com.dongsan.domain.domains.crew.infrastructure.CrewMemberCoreRepository;
import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberCoreRepository;
import com.dongsan.domain.domains.member.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CowalkPostFacadeTest extends IntegrationTest {

    @Autowired
    private CowalkPostFacade cowalkPostFacade;

    @Autowired
    private CowalkParticipantCoreRepository cowalkParticipantRepository;

    @Autowired
    private CowalkPostCoreRepository cowalkPostRepository;

    @Autowired
    private MemberCoreRepository memberRepository;

    @Autowired
    private CrewMemberCoreRepository crewMemberRepository;

    @Autowired
    private CrewCoreRepository crewRepository;

    @BeforeEach
    void setUpPost() {

        Capacity capacity = new Capacity(false, 100);
        Crew crew = new PublicCrew("name", "description", "rule", "image", capacity);

        crewRepository.save(crew);

        for (int i = 0; i < 1000; i++) {
            Member member = memberRepository.save("email", "nickname", "profile", MemberRole.ROLE_USER, Provider.KAKAO);
            crewMemberRepository.save(
                    new CrewMember(crew.getId(), member.getId(), CrewMemberRole.PARTICIPANT)
            );
        }

        // given
        Integer limit = 100;
        Long crewId = 1L;
        Long memberId = 1L;

        CreateCowalkPostCommand command
                = new CreateCowalkPostCommand(crewId, memberId, LocalDate.now(), LocalTime.now(), limit);

        // 참가 최대 인원 30인 게시글 생성
        CowalkPost post = new CowalkPost(command);
        cowalkPostRepository.save(post);
    }

    @Test
    void 동시에_100명이_참여하면_30명만_저장된다() throws InterruptedException {
        int executeCount = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(executeCount);

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

        // 결과 확인
        Integer count = cowalkParticipantRepository.countByCowalkPostId(1L);
        assertThat(count).isEqualTo(100);
    }
}