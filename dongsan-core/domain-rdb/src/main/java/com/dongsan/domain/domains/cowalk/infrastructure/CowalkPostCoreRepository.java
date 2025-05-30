package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkPost;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CowalkPostCoreRepository implements CowalkPostRepository {

	private final CowalkPostJpaRepository cowalkPostJpaRepository;
	private final JPAQueryFactory queryFactory;

	private final QCowalkPost qCowalkPost = QCowalkPost.cowalkPost;

	public CowalkPostCoreRepository(CowalkPostJpaRepository cowalkPostJpaRepository, JPAQueryFactory queryFactory) {
		this.cowalkPostJpaRepository = cowalkPostJpaRepository;
		this.queryFactory = queryFactory;
	}

	@Override
	public void save(CowalkPost cowalkPost) {
		cowalkPostJpaRepository.save(cowalkPost);
	}
}
