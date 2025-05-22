package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import com.dongsan.rdb.domains.walkway.infrastructure.LikedWalkwayRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LikedWalkwayRdbService {
    private final LikedWalkwayRepository likedWalkwayRepository;

    public LikedWalkwayRdbService(LikedWalkwayRepository likedWalkwayRepository) {
        this.likedWalkwayRepository = likedWalkwayRepository;
    }

    public Optional<LikedWalkway> getOptionalLikedWalkway(Long member, Long walkwayId) {
        return likedWalkwayRepository.findByMemberIdAndWalkwayId(member, walkwayId);
    }

    public void save(Long memberId, Long walkwayId) {
        Optional<LikedWalkway> optionalLikedWalkway = getOptionalLikedWalkway(memberId, walkwayId);
        if (optionalLikedWalkway.isPresent()) {
            throw new CoreException(CoreErrorCode.ALREADY_LIKED_WALKWAY);
        }
        LikedWalkway likedWalkway = new LikedWalkway(memberId, walkwayId);
        likedWalkwayRepository.save(likedWalkway);
    }

    public void delete(Long memberId, Long walkwayId) {
        Optional<LikedWalkway> optionalLikedWalkway = getOptionalLikedWalkway(memberId, walkwayId);
        if (optionalLikedWalkway.isEmpty()) {
            throw new CoreException(CoreErrorCode.NOT_LIKED_WALKWAY);
        }
        LikedWalkway likedWalkway = optionalLikedWalkway.get();
        likedWalkwayRepository.delete(likedWalkway);
    }

    public boolean isLiked(Long memberId, Long walkwayId) {
        return getOptionalLikedWalkway(memberId, walkwayId).isPresent();
    }
}
