package com.dongsan.domain.domains.cowalk.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkCommentRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkComment;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CowalkCommentCoreRepository implements CowalkCommentRepository {
	private final CowalkCommentJpaRepository cowalkCommentJpaRepository;
	private final JPAQueryFactory queryFactory;

	private final QCowalkComment cowalkComment = QCowalkComment.cowalkComment;

	public CowalkCommentCoreRepository(
		CowalkCommentJpaRepository cowalkCommentJpaRepository,
		JPAQueryFactory queryFactory
	) {
		this.cowalkCommentJpaRepository = cowalkCommentJpaRepository;
		this.queryFactory = queryFactory;
	}

	@Override
	public Integer countByCowalkPostId(Long cowalkPostId) {
		return cowalkCommentJpaRepository.countByCowalkPostId(cowalkPostId);
	}

	@Override
	public Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds) {
		List<Tuple> countTuple = queryFactory
			.select(cowalkComment.cowalkPostId, cowalkComment.count())
			.from(cowalkComment)
			.where(cowalkComment.cowalkPostId.in(cowalkPostIds))
			.groupBy(cowalkComment.cowalkPostId)
			.fetch();

		return countTuple.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(cowalkComment.cowalkPostId),
				tuple -> tuple.get(cowalkComment.count()).intValue()
			));
	}
}
