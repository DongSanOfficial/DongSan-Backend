package com.dongsan.rdb.domains.bookmark.infrastructure;

import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkway;
import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkwayRepository;
import com.dongsan.rdb.domains.bookmark.domain.QBookmark;
import com.dongsan.rdb.domains.bookmark.domain.QMarkedWalkway;
import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.QWalkway;
import com.dongsan.rdb.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MarkedWalkwayCoreRepository implements MarkedWalkwayRepository {
    private final MarkedWalkwayJpaRepository markedWalkwayJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;
    private QWalkway walkway = QWalkway.walkway;
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
    public CursorPage<MarkedWalkway> getBookmarkWalkway(Long memberId, Long bookmarkId, LocalDateTime lastCreatedAt, int size) {
        List<MarkedWalkway> result = queryFactory.selectFrom(markedWalkway)
                .join(walkway).on(walkway.id.eq(markedWalkway.walkwayId))
                .where(markedWalkway.bookmarkId.eq(bookmarkId),
                        walkway.walkwayInfo.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkway.memberId.eq(memberId)),
                        markedBookmarkCreatedAtLt(lastCreatedAt))
                .limit(size + 1)
                .orderBy(markedWalkway.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    /**
     * 북마크에 추가된 시간이 walkwayId가 북마크에 추가된 시간보다 작아야 한다. (markedBookmark의 createdAt이 더 작은 markedWalkay를 조회)
     *
     * @param lastCreatedAt 마지막으로 가져온 markedBookmark의 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression markedBookmarkCreatedAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null;
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

    @Override
    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        markedWalkwayJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

}
