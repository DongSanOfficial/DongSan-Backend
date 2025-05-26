package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import com.dongsan.rdb.domains.walkway.domain.LikedWalkwayRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
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

    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        likedWalkwayRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    public int countLikes(Long walkwayId) {
        return likedWalkwayRepository.countByWalkwayId(walkwayId);
    }

    // {walkwayId, likeCount}
    public Map<Long, Long> countLikesMap(List<Long> walkwayIds) {
        return likedWalkwayRepository.countByWalkwayIds(walkwayIds);
    }

    // {walkwayId, ...}
    public Set<Long> likedWalkways(Long memberId, List<Long> walkwayIds) {
        return likedWalkwayRepository.getLikedWalkways(memberId, walkwayIds);
    }
}
