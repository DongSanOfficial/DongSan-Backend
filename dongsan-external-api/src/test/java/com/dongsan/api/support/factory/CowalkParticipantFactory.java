package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.CowalkParticipantRepository;

@Component
public class CowalkParticipantFactory {
    @Autowired
    private CowalkParticipantRepository cowalkParticipantRepository;

    public void save(Long cowalkPostId, Long memberId) {
        cowalkParticipantRepository.save(new CowalkParticipant(cowalkPostId, memberId));
    }
}
