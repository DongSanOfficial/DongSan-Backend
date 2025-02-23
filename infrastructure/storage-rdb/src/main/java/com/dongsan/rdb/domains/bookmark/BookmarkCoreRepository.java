package com.dongsan.rdb.domains.bookmark;

import static com.querydsl.core.group.GroupBy.groupBy;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkRepository;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.bookmark.entity.QBookmark;
import com.dongsan.rdb.domains.bookmark.entity.QMarkedWalkway;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkCoreRepository implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final MarkedWalkwayJpaRepository markedWalkwayJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QBookmark bookmark = QBookmark.bookmark;
    private QMarkedWalkway markedWalkway = QMarkedWalkway.markedWalkway;


    public BookmarkCoreRepository(BookmarkJpaRepository bookmarkJpaRepository,
                                  MarkedWalkwayJpaRepository markedWalkwayJpaRepository, MemberJpaRepository memberJpaRepository,
                                  WalkwayJpaRepository walkwayJpaRepository, JPAQueryFactory queryFactory) {
        this.bookmarkJpaRepository = bookmarkJpaRepository;
        this.markedWalkwayJpaRepository = markedWalkwayJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Long memberId, String name) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        BookmarkEntity bookmarkEntity = new BookmarkEntity(name, memberEntity);
        return bookmarkJpaRepository.save(bookmarkEntity).getId();
    }

    @Override
    public boolean existsById(Long bookmarkId) {
        return bookmarkJpaRepository.existsById(bookmarkId);
    }

    @Override
    public Optional<Bookmark> findById(Long bookmarkId) {
        return bookmarkJpaRepository.findById(bookmarkId)
                .map(BookmarkEntity::toBookmark);
    }

    @Override
    public boolean existsByMemberIdAndName(Long memberId, String name) {
        return bookmarkJpaRepository.existsByMemberIdAndName(memberId, name);
    }

    @Override
    public void rename(Long bookmarkId, String name) {
        BookmarkEntity bookmarkEntity = bookmarkJpaRepository.getReferenceById(bookmarkId);
        bookmarkEntity.rename(name);
    }

    @Override
    public boolean isWalkwayAdded(Long bookmarkId, Long walkwayId) {
        return markedWalkwayJpaRepository.existsByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }

    @Override
    public void includeWalkway(Long bookmarkId, Long walkwayId) {
        BookmarkEntity bookmarkEntity = bookmarkJpaRepository.getReferenceById(bookmarkId);
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(walkwayId);
        MarkedWalkwayEntity markedWalkway = new MarkedWalkwayEntity(bookmarkEntity, walkwayEntity);
        markedWalkwayJpaRepository.save(markedWalkway);
    }

    @Override
    public void excludeWalkway(Long bookmarkId, Long walkwayId) {
        markedWalkwayJpaRepository.deleteByBookmarkIdAndWalkwayId(bookmarkId, walkwayId);
    }

    @Override
    public void deleteById(Long bookmarkId) {
        bookmarkJpaRepository.deleteById(bookmarkId);
        markedWalkwayJpaRepository.deleteAllByBookmarkId(bookmarkId);
    }

    @Override
    public Optional<LocalDateTime> getBookmarkedDate(Long bookmarkId, Long walkwayId) {
        return markedWalkwayJpaRepository.findByBookmarkIdAndWalkwayId(bookmarkId, walkwayId).map(
                BaseEntity::getCreatedAt);
    }


    @Override
    public List<MarkedWalkway> getBookmarkWalkways(Long bookmarkId, int size,
                                                   LocalDateTime lastCreatedAt, Long memberId) {
        List<MarkedWalkwayEntity> markedWalkwayEntities = queryFactory.select(markedWalkway)
                .from(markedWalkway)
                .join(markedWalkway.walkway).fetchJoin()
                .where(markedWalkway.bookmark.id.eq(bookmarkId), markedBookmarkCreatedAtLt(lastCreatedAt),
                        markedWalkway.walkway.member.id.eq(memberId)
                                .or(markedWalkway.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .orderBy(markedWalkway.createdAt.desc())
                .limit(size)
                .fetch();
        return markedWalkwayEntities.stream().map(MarkedWalkwayEntity::toMarkedWalkway).toList();
    }

    /**
     * 북마크에 추가된 시간이 walkwayId가 북마크에 추가된 시간보다 작아야 한다. (markedBookmark의 createdAt이 더 작은 markedWalkay를 조회)
     * @param lastCreatedAt 마지막으로 가져온 markedBookmark의 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression markedBookmarkCreatedAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt != null ? markedWalkway.createdAt.lt(lastCreatedAt) : null;
    }

    @Override
    public List<Bookmark> getUserBookmarks(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        List<BookmarkEntity> bookmarkEntities = queryFactory.selectFrom(bookmark)
                .where(bookmark.member.id.eq(memberId),
                        bookmarkCreatedAtLt(lastCreatedAt))
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();
        return bookmarkEntities.stream().map(BookmarkEntity::toBookmark).toList();
    }

    @Override
    public List<BookmarkWithMarkedStatus> getBookmarksWithMarkedWalkway(Long walkwayId, Long memberId,
                                                                        LocalDateTime lastCreatedAt, Integer size) {
        List<BookmarksWithMarkedWalkwayDTO> result = queryFactory.select(Projections.constructor(
                        BookmarksWithMarkedWalkwayDTO.class,
                        bookmark.id,
                        bookmark.name,
                        bookmark.createdAt,
                        markedWalkway.id
                ))
                .from(bookmark)
                .leftJoin(markedWalkway)
                .on(markedWalkway.walkway.id.eq(walkwayId).and(markedWalkway.bookmark.id.eq(bookmark.id)))
                .where(bookmark.member.id.eq(memberId),
                        bookmarkCreatedAtLt(lastCreatedAt))
                .limit(size)
                .orderBy(bookmark.createdAt.desc())
                .fetch();

        return result.stream().map(BookmarksWithMarkedWalkwayDTO::toBookmarkWithMarkedStatus).toList();
    }

    private BooleanExpression bookmarkCreatedAtLt(LocalDateTime lastCreatedAt){
        return lastCreatedAt == null ? null : bookmark.createdAt.lt(lastCreatedAt);
    }

    public Map<Long, Boolean> existsMarkedWalkway(Long walkwayId, List<Long> bookmarkIds) {
        return queryFactory.from(markedWalkway)
                .where(
                        markedWalkway.walkway.id.eq(walkwayId),
                        markedWalkway.bookmark.id.in(bookmarkIds)
                )
                .transform(groupBy(markedWalkway.bookmark.id).as(markedWalkway.isNotNull()));
    }

    @Override
    public boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId) {
        return queryFactory
                .selectOne()
                .from(markedWalkway)
                .where(
                        markedWalkway.walkway.id.eq(walkwayId),
                        markedWalkway.bookmark.member.id.eq(memberId)
                )
                .fetchFirst() != null;
    }

}
