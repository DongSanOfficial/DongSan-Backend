package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.bookmark.domain.MarkedWalkwayRepository;

@Component
public class MarkedWalkwayFactory {
    @Autowired
    private MarkedWalkwayRepository markedWalkwayRepository;

    public void save(Long bookmarkId, Long walkwayId) {
        markedWalkwayRepository.includeWalkway(bookmarkId, walkwayId);
    }
}
