package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.walkway.domain.LikedWalkway;
import com.dongsan.domain.domains.walkway.domain.LikedWalkwayRepository;

@Component
public class LikedWalkwayFactory {
    @Autowired
    private LikedWalkwayRepository likedWalkwayRepository;

    public void save(Long memberId, Long walkwayId) {
        likedWalkwayRepository.save(new LikedWalkway(memberId, walkwayId));
    }
}
