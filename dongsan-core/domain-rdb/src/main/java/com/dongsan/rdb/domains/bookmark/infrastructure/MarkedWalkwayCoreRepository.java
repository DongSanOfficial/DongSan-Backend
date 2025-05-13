package com.dongsan.rdb.domains.bookmark.infrastructure;

import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkway;
import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkwayRepository;
import com.dongsan.rdb.domains.bookmark.domain.QBookmark;
import com.dongsan.rdb.domains.bookmark.domain.QMarkedWalkway;
import com.dongsan.rdb.domains.common.BaseEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class MarkedWalkwayCoreRepository implements MarkedWalkwayRepository {
    private final MarkedWalkwayJpaRepository markedWalkwayJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;
    private QBookmark bookmark = QBookmark.bookmark;

    public MarkedWalkwayCoreRepository(MarkedWalkwayJpaRepository markedWalkwayJpaRepository, JPAQueryFactory queryFactory) {
        this.markedWalkwayJpaRepository = markedWalkwayJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public boolean isWalkwayAdded(Long bookmarkId, Long walkwayId) {
        return markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }


    @Override
    public Optional<LocalDateTime> getBookmarkedDate(Long bookmarkId, Long walkwayId) {
        return markedWalkwayJpaRepository.findByBookmarkIdAndWalkwayId(bookmarkId, walkwayId)
                .map(BaseEntity::getCreatedAt);
    }

    @Override
    public boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId) {
        return queryFactory
                .selectOne()
                .from(markedWalkway)
                .join(bookmark).on(markedWalkway.bookmarkId.eq(bookmark.id))
                .where(
                        markedWalkway.walkwayId.eq(walkwayId),
                        bookmark.memberId.eq(memberId)
                )
                .fetchFirst() != null;
    }

    @Override
    public void includeWalkway(Long bookmarkId, Long walkwayId) {
        MarkedWalkway markedWalkway = new MarkedWalkway(bookmarkId, walkwayId);
        markedWalkwayJpaRepository.save(markedWalkway);
    }

    @Override
    public void excludeWalkway(Long bookmarkId, Long walkwayId) {
        markedWalkwayJpaRepository.deleteByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }

    @Override
    public void deleteAllByBookmarkId(Long bookmarkId) {
        markedWalkwayJpaRepository.deleteAllByBookmarkId(bookmarkId);
    }

    // 산책로가 북마크에 추가되었는지 유무
//    public Map<Long, Boolean> existsMarkedWalkway(Long walkwayId, List<Long> bookmarkIds) {
//        List<Tuple> result = queryFactory
//                .select(
//                        bookmark.id,
//                        markedWalkway.id.isNotNull()
//                )
//                .from(bookmark)
//                .leftJoin(markedWalkway)
//                .on(markedWalkway.walkway.id.eq(walkwayId))
//                .on(markedWalkway.bookmark.id.eq(bookmark.id))
//                .where(bookmark.id.in(bookmarkIds))
//                .fetch();
//
//        // 결과를 Map<Long, Boolean>으로 변환
//        Map<Long, Boolean> resultMap = new HashMap<>();
//        for (Tuple tuple : result) {
//            Long bookmarkId = tuple.get(bookmark.id);
//            Boolean marked = tuple.get(markedWalkway.id.isNotNull());
//            resultMap.put(bookmarkId, marked);
//        }
//
//        return resultMap;
//    }
}
