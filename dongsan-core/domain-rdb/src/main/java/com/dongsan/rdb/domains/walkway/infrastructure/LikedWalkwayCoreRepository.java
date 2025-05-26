package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import com.dongsan.rdb.domains.walkway.domain.QLikedWalkway;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
public class LikedWalkwayCoreRepository implements LikedWalkwayRepository {
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QLikedWalkway likedWalkway = QLikedWalkway.likedWalkway;

    public LikedWalkwayCoreRepository(LikedWalkwayJpaRepository likedWalkwayJpaRepository, JPAQueryFactory queryFactory) {
        this.likedWalkwayJpaRepository = likedWalkwayJpaRepository;
        this.queryFactory = queryFactory;
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

    @Override
    public Map<Long, Long> countByWalkwayIds(List<Long> walkwayIds) {
        return queryFactory.from(likedWalkway)
                .where(likedWalkway.walkwayId.in(walkwayIds))
                .groupBy(likedWalkway.walkwayId)
                .transform(groupBy(likedWalkway.walkwayId).as(likedWalkway.count()));
    }
}
