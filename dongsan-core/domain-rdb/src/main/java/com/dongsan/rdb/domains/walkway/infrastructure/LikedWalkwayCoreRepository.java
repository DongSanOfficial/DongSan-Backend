package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LikedWalkwayCoreRepository implements LikedWalkwayRepository {
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;

    public LikedWalkwayCoreRepository(LikedWalkwayJpaRepository likedWalkwayJpaRepository) {
        this.likedWalkwayJpaRepository = likedWalkwayJpaRepository;
    }

    @Override
    public Optional<LikedWalkway> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId) {
        return likedWalkwayJpaRepository.findByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public void save(LikedWalkway likedWalkway) {
        likedWalkwayJpaRepository.save(likedWalkway);
    }

    @Override
    public void delete(LikedWalkway likedWalkway) {
        likedWalkwayJpaRepository.delete(likedWalkway);
    }

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        likedWalkwayJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    @Override
    public int countByWalkwayId(Long walkwayId) {
        return likedWalkwayJpaRepository.countByWalkwayId(walkwayId);
    }
}
