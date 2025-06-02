package com.dongsan.domain.domains.cowalk.infrastructure;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkPost;
import com.dongsan.domain.support.paging.CursorResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CowalkPostCoreRepository implements CowalkPostRepository {

    private final CowalkPostJpaRepository cowalkPostJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QCowalkPost cowalkPost = QCowalkPost.cowalkPost;

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
}
