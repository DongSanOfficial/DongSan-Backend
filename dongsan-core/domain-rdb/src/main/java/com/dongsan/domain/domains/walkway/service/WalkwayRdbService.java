package com.dongsan.domain.domains.walkway.service;

import com.dongsan.domain.domains.walkway.CreateWalkwayCommand;
import com.dongsan.domain.domains.walkway.LineStringMapper;
import com.dongsan.domain.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwayGeometry;
import com.dongsan.domain.domains.walkway.domain.WalkwayInfo;
import com.dongsan.domain.domains.walkway.domain.WalkwayRepository;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.util.CursorPage;
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
        return walkwayRepository.findById(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
    }

//    public Optional<Walkway> findById(Long walkwayId) {
//        return walkwayRepository.findById(walkwayId);
//    }

    public Walkway getWalkwayWithAccessValidation(Long walkwayId, Long memberId) {
        Walkway walkway = getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        return walkway;
    }

    private LocalDateTime getWalkwayCreatedAt(Long walkwayId, Long memberId) {
        if (walkwayId == null) {
            return null;
        }

        Optional<Walkway> optionalWalkway = walkwayRepository.findById(walkwayId);
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
