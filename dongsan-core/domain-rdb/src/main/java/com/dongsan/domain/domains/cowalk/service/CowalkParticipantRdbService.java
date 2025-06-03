package com.dongsan.domain.domains.cowalk.service;

import static com.dongsan.domain.support.error.CoreErrorCode.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.CowalkParticipantRepository;
import com.dongsan.domain.support.error.CoreException;

@Service
@Transactional
public class CowalkParticipantRdbService {
    private final CowalkParticipantRepository cowalkParticipantRepository;

    public CowalkParticipantRdbService(CowalkParticipantRepository cowalkParticipantRepository) {
        this.cowalkParticipantRepository = cowalkParticipantRepository;
    }

    public Long save(Long memberId, Long cowalkPostId) {
        validAlreadyJoin(memberId, cowalkPostId);
        CowalkParticipant cowalkParticipant = new CowalkParticipant(cowalkPostId, memberId);
        cowalkParticipantRepository.save(cowalkParticipant);
        return cowalkParticipant.getId();
    }

    public Integer countByCowalkPostId(Long cowalkPostId) {
        return cowalkParticipantRepository.countByCowalkPostId(cowalkPostId);
    }

    public Boolean isJoin(Long memberId, Long cowalkPostId) {
        return cowalkParticipantRepository.existsByMemberIdAndCowalkPostId(memberId, cowalkPostId);
    }

    public void validAlreadyJoin(Long memberId, Long cowalkPostId) {
        if (isJoin(memberId, cowalkPostId)) {
            throw new CoreException(COWALK_PARTICIPANT_ALREADY_JOIN);
        }
    }
    
    public void validNotJoin(Long memberId, Long cowalkPostId) {
        if (!isJoin(memberId, cowalkPostId)) {
            throw new CoreException(COWALK_PARTICIPANT_NOT_JOIN);
        }
    }

    public Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds) {
        return cowalkParticipantRepository.countByCowalkPostIds(cowalkPostIds);
    }
}
