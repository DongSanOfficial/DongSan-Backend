package com.dongsan.domain.domains.cowalk.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkPost;
import com.dongsan.domain.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
	public CursorPage<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId) {
		List<CowalkPost> cowalkPosts = queryFactory.selectFrom(cowalkPost)
			.where(
				cowalkPost.crewId.eq(crewId),
				cowalkPostIdLt(lastId)
			)
			.orderBy(cowalkPost.id.desc())
			.limit(size + 1L)
			.fetch();

		return new CursorPage<>(cowalkPosts, size);
	}

	private BooleanExpression cowalkPostIdLt(Long id) {
		return id == null ? null : cowalkPost.id.lt(id);
	}
}
