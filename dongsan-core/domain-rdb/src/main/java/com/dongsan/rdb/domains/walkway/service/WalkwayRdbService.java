package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.CreateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.LineStringMapper;
import com.dongsan.rdb.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.domain.WalkwayGeometry;
import com.dongsan.rdb.domains.walkway.domain.WalkwayInfo;
import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalkwayRdbService {
    private final WalkwayRepository walkwayRepository;

    public WalkwayRdbService(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.getWalkway(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
    }

    public Walkway getWalkwayWithAccessValidation(Long walkwayId, Long memberId) {
        Walkway walkway = getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        return walkway;
    }

    public Long save(CreateWalkwayCommand command) {
        LineString course = LineStringMapper.toLineString(command.course());
        WalkwayGeometry walkwayGeometry = new WalkwayGeometry(course, command.imageUrl());
        WalkwayInfo walkwayInfo = new WalkwayInfo(command.name(), command.distance(), command.time(), command.exposeLevel(), command.memo(), command.hashtags());
        Walkway walkway = new Walkway(command.memberId(), walkwayInfo, walkwayGeometry);
        return walkwayRepository.save(walkway);
    }

    // TODO : 이렇게 하면 hashtag 바로 수정되나..? 그리고 필드 굳이 다 넘길 필요 있는지도 고민
    public void update(UpdateWalkwayCommand command, Long memberId) {
        Walkway walkway = getWalkway(command.walkwayId());
        walkway.isOwner(memberId);
        walkway.updateWalkway(command.name(), command.memo(), command.exposeLevel(), command.hashtags());
    }

    public void delete(Long walkwayId, Long memberId) {
        Walkway walkway = getWalkway(walkwayId);
        walkway.isOwner(memberId);
        walkwayRepository.delete(walkway);
    }


}
