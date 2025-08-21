package com.dongsan.domain.domains.cowalk.infrastructure;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.QCowalkPost;
import com.dongsan.domain.support.paging.CursorResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CowalkPostCoreRepository implements CowalkPostRepository {

    private final CowalkPostJpaRepository cowalkPostJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QCowalkPost cowalkPost = QCowalkPost.cowalkPost;
    private final QCowalkParticipant cowalkParticipant = QCowalkParticipant.cowalkParticipant;

    public CowalkPostCoreRepository(CowalkPostJpaRepository cowalkPostJpaRepository, JPAQueryFactory queryFactory) {
        this.cowalkPostJpaRepository = cowalkPostJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(CowalkPost cowalkPost) {
        cowalkPostJpaRepository.save(cowalkPost);
    }

    @Override
    public Optional<CowalkPost> findById(Long id) {
        return cowalkPostJpaRepository.findById(id);
    }

    @Override
    public CursorResponse<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId) {
        List<CowalkPost> cowalkPosts = queryFactory.selectFrom(cowalkPost)
                .where(
                        cowalkPost.crewId.eq(crewId),
                        cowalkPostIdLt(lastId)
                )
                .orderBy(cowalkPost.id.desc())
                .limit(size + 1L)
                .fetch();

        return new CursorResponse<>(cowalkPosts, size);
    }

    private BooleanExpression cowalkPostIdLt(Long id) {
        return id == null ? null : cowalkPost.id.lt(id);
    }

    @Override
    public CursorResponse<CowalkPost> getJoinedCowalkPost(Long memberId, Long lastId, int size) {
        CowalkPost lastCowalkPost = this.getCowalkPostEntity(lastId);
        LocalDateTime now = LocalDateTime.now();

        List<CowalkPost> cowalkPosts = queryFactory.selectFrom(cowalkPost)
                .join(cowalkParticipant).on(cowalkPost.id.eq(cowalkParticipant.cowalkPostId))
                .where(cowalkParticipant.memberId.eq(memberId),
                        cowalkPost.endedAt.gt(now),
                        cowalkPostStartedAtGt(lastCowalkPost)
                )
                .orderBy(cowalkPost.startedAt.asc(), cowalkPost.id.asc())
                .limit(size + 1L)
                .fetch();

        return new CursorResponse<>(cowalkPosts, size);
    }

    private BooleanExpression cowalkPostStartedAtGt(CowalkPost lastCowalkPost) {
        if (lastCowalkPost == null) {
            return null;
        }
        return cowalkPost.startedAt.gt(lastCowalkPost.getStartedAt())
                .or(cowalkPost.startedAt.eq(lastCowalkPost.getStartedAt()).and(cowalkPost.id.gt(lastCowalkPost.getId())));
    }

    // 마지막 산책로 엔티티 조회
    private CowalkPost getCowalkPostEntity(Long cowalkId) {
        if (cowalkId == null) {
            return null;
        }
        return queryFactory.selectFrom(cowalkPost)
                .where(cowalkPost.id.eq(cowalkId))
                .fetchOne();
    }
}
