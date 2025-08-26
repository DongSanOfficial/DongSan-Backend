package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRepository;

@Component
public class WalkwayLogFactory {
    @Autowired
    private WalkwayLogRepository walkwayLogRepository;

    public void save(Long memberId, Long walkwayId, Integer time, Double distance) {
        walkwayLogRepository.save(new WalkwayLog(memberId, walkwayId, time, distance));
    }
}
