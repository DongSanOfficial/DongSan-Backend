package com.dongsan.api.support.factory;

import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalkwayLogFactory {
    @Autowired
    private WalkwayLogRepository walkwayLogRepository;

    public Long save(Long memberId, Long walkwayId, Integer time, Double distance) {
        return walkwayLogRepository.save(new WalkwayLog(memberId, walkwayId, time, distance));
    }
}
