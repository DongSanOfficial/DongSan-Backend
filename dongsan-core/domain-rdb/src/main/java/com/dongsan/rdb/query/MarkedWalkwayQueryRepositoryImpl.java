package com.dongsan.rdb.query;

import com.dongsan.rdb.domains.bookmark.domain.QMarkedWalkway;
import com.dongsan.rdb.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class MarkedWalkwayQueryRepositoryImpl implements MarkedWalkwayQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;
    //private final QWalkway walkway = QWalkway

    public MarkedWalkwayQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    // TODO : 엔티티 위치 변경 후에 추가해야 할듯... 흐음...
    @Override
    public CursorPage<MarkedWalkwayParam> getBookmarkWalkway(Long memberId, Long bookmarkId, LocalDateTime lastCreatedAt, int size) {
//        return queryFactory.select(markedWalkway)
//                .from(markedWalkway)
//                .join(markedWalkway.wa)
//                .fetchJoin()
//                .where(markedWalkway.bookmarkId.eq(bookmarkId),
//                        markedBookmarkCreatedAtLt(lastCreatedAt),
//                        markedWalkway.walkway.member.id.eq(memberId)
//                                .or(markedWalkway.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
//                .orderBy(markedWalkway.createdAt.desc())
//                .limit(size)
//                .fetch();

//        return queryFactory
//                .select(Projections.constructor(
//                        MarkedWalkwayWithSummary.class,
//                        markedWalkway.walkwayId,
//                        walkway.name,
//                        walkway.distance,
//                        walkway.imageUrl,
//                        markedWalkway.createdAt
//                ))
//                .from(markedWalkway)
//                .join(walkway).on(markedWalkway.walkwayId.eq(walkway.id))
//                .where(
//                        markedWalkway.bookmarkId.eq(bookmarkId),
//                        lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null,
//                        walkway.exposeLevel.eq(ExposeLevel.PUBLIC)
//                                .or(walkway.member.id.eq(memberId))
//                )
//                .orderBy(markedWalkway.createdAt.desc())
//                .limit(size + 1)
//                .fetch();
        return null;
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
}
