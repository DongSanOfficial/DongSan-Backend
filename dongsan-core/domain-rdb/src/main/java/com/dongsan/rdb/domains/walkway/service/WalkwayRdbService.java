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
import com.dongsan.rdb.support.util.CursorPage;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private LocalDateTime getWalkwayCreatedAt(Long walkwayId, Long memberId) {
        Optional<Walkway> optionalWalkway = walkwayRepository.getWalkway(walkwayId);
        if (optionalWalkway.isEmpty()) {
            return null;
        }
        Walkway walkway = optionalWalkway.get();
        walkway.validateAccess(memberId);
        return walkway.getCreatedAt();
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

    public CursorPage<Walkway> getUserWalkway(Long memberId, Long lastWalkwayId, int size) {
        LocalDateTime lastCreatedAt = getWalkwayCreatedAt(lastWalkwayId, memberId);
        return walkwayRepository.getUserWalkway(memberId, lastCreatedAt, size);
    }

    public CursorPage<Walkway> getUserLikedWalkway(Long memberId, Long lastWalkwayId, int size) {
        LocalDateTime lastCreatedAt = getWalkwayCreatedAt(lastWalkwayId, memberId);
        return walkwayRepository.getUserLikedWalkway(memberId, lastCreatedAt, size);
    }

    // {walkwayId, Walkway}
    public Map<Long, Walkway> getWalkways(List<Long> walkwayIds) {
        return walkwayRepository.getWalkways(walkwayIds);
    }


    public CursorPage<Walkway> getWalkwaysLatest(Long memberId, Long lastWalkwayId, int size) {
        return walkwayRepository.getWalkwaysByLatest(memberId, lastWalkwayId, size);
    }

    public CursorPage<Walkway> getWalkwaysLiked(Long memberId, Long lastWalkwayId, int size) {
        return walkwayRepository.getWalkwaysByLiked(memberId, lastWalkwayId, size);
    }

    public CursorPage<Walkway> getWalkwaysRating(Long memberId, Long lastWalkwayId, int size) {
        return walkwayRepository.getWalkwaysByRating(memberId, lastWalkwayId, size);
    }


}
