package com.dongsan.domain.domains.cowalk.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkParticipant;
import com.dongsan.domain.domains.cowalk.domain.CowalkParticipantRepository;
import com.dongsan.domain.domains.cowalk.domain.QCowalkParticipant;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CowalkParticipantCoreRepository implements CowalkParticipantRepository {
	private final CowalkParticipantJpaRepository cowalkParticipantJpaRepository;
	private final JPAQueryFactory queryFactory;

	private final QCowalkParticipant cowalkParticipant = QCowalkParticipant.cowalkParticipant;

	public CowalkParticipantCoreRepository(
		CowalkParticipantJpaRepository cowalkParticipantJpaRepository,
		JPAQueryFactory queryFactory
	) {
		this.cowalkParticipantJpaRepository = cowalkParticipantJpaRepository;
		this.queryFactory = queryFactory;
	}

	@Override
	public CowalkParticipant save(CowalkParticipant cowalkParticipant) {
		return cowalkParticipantJpaRepository.save(cowalkParticipant);
	}

	@Override
	public Integer countByCowalkPostId(Long cowalkPostId) {
		return cowalkParticipantJpaRepository.countByCowalkPostId(cowalkPostId);
	}

	@Override
	public Boolean existsByMemberIdAndCowalkPostId(Long memberId, Long cowalkPostId) {
		return cowalkParticipantJpaRepository.existsByMemberIdAndCowalkPostId(memberId, cowalkPostId);
	}

	public Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds) {
		List<Tuple> countTuple = queryFactory
			.select(cowalkParticipant.cowalkPostId, cowalkParticipant.count())
			.from(cowalkParticipant)
			.where(cowalkParticipant.cowalkPostId.in(cowalkPostIds))
			.groupBy(cowalkParticipant.cowalkPostId)
			.fetch();

		return countTuple.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(cowalkParticipant.cowalkPostId),
				tuple -> tuple.get(cowalkParticipant.count()).intValue()
			));
	}
}
