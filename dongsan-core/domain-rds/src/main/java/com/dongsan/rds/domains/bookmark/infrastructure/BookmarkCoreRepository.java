package com.dongsan.rds.domains.bookmark.infrastructure;


import com.dongsan.rds.common.CursorPage;
import com.dongsan.rds.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.rds.domains.bookmark.BookmarkWithMarkedWalkwayParam;
import com.dongsan.rds.domains.bookmark.domain.Bookmark;
import com.dongsan.rds.domains.bookmark.domain.BookmarkRepository;
import com.dongsan.rds.domains.bookmark.domain.QBookmark;
import com.dongsan.rds.domains.bookmark.domain.QMarkedWalkway;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookmarkCoreRepository implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;
    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;

    public BookmarkCoreRepository(BookmarkJpaRepository bookmarkJpaRepository,
                                  JPAQueryFactory queryFactory) {
        this.bookmarkJpaRepository = bookmarkJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Bookmark bookmark) {
        return bookmarkJpaRepository.save(bookmark).getId();
    }

    @Override
    public Optional<Bookmark> findById(Long bookmarkId) {
        return bookmarkJpaRepository.findById(bookmarkId);
    }

    @Override
    public boolean existsByMemberIdAndName(Long memberId, String name) {
        return bookmarkJpaRepository.existsByMemberIdAndName(memberId, name);
    }

    @Override
    public void deleteById(Long bookmarkId) {
        bookmarkJpaRepository.deleteById(bookmarkId);
    }

    /**
     * 자신이 등록한 북마크 반환 (생성시간 기준 내림차순 정렬)
     */
    @Override
    public CursorPage<Bookmark> getUserBookmarks(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<Bookmark> result = queryFactory.selectFrom(bookmark)
                .where(bookmark.memberId.eq(memberId),
                        bookmarkCreatedAtLt(lastCreatedAt))
                .limit(size + 1)
                .orderBy(bookmark.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    /**
     * 사용자가 생성한 북마크 중에서 특정 산책로가 포함되어 있는지 아닌지를 표시
     * (북마크 생성시간 기준 내림차순 정렬)
     */
    @Override
    public CursorPage<BookmarkWithMarkedStatus> getBookmarksWithMarkedStatus(Long walkwayId, Long memberId,
                                                                             LocalDateTime lastCreatedAt, int size) {
        List<BookmarkWithMarkedWalkwayParam> result = queryFactory.select(Projections.constructor(
                        BookmarkWithMarkedWalkwayParam.class,
                        bookmark.id,
                        bookmark.name,
                        bookmark.createdAt,
                        markedWalkway.id
                ))
                .from(bookmark)
                .leftJoin(markedWalkway)
                .on(markedWalkway.walkwayId.eq(walkwayId)
                        .and(markedWalkway.bookmarkId.eq(bookmark.id)))
                .where(bookmark.memberId.eq(memberId),
                        bookmarkCreatedAtLt(lastCreatedAt))
                .limit(size + 1)
                .orderBy(bookmark.createdAt.desc())
                .fetch();

        List<BookmarkWithMarkedStatus> streamResult = result.stream()
                .map(BookmarkWithMarkedWalkwayParam::toBookmarkWithMarkedStatus)
                .toList();
        return new CursorPage<>(streamResult, size);
    }

    private BooleanExpression bookmarkCreatedAtLt(LocalDateTime lastCreatedAt) {
        return lastCreatedAt == null ? null : bookmark.createdAt.lt(lastCreatedAt);
    }

}
