package com.dongsan.api.support.factory;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;

@Component
public class CowalkPostFactory {
    @Autowired
    private CowalkPostRepository cowalkPostRepository;

    public void save(Long crewId, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        CreateCowalkPostCommand command = new CreateCowalkPostCommand(crewId, memberId, now, now.plusHours(1), 10, "test");
        cowalkPostRepository.save(new CowalkPost(command));
    }
}
