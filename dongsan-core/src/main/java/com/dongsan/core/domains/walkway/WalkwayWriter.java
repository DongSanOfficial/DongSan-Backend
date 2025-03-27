package com.dongsan.core.domains.walkway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalkwayWriter {

	@Autowired
	public WalkwayWriter(WalkwayRepository walkwayRepository) {
		this.walkwayRepository = walkwayRepository;
	}

	private final WalkwayRepository walkwayRepository;

	public Long saveWalkway(CreateWalkway createWalkway) {
		return walkwayRepository.saveWalkway(createWalkway);
	}

	public void updateWalkway(UpdateWalkway updateWalkway) {
		walkwayRepository.updateWalkway(updateWalkway);
	}

	public void deleteWalkway(Long walkwayId) {
		walkwayRepository.deleteWalkway(walkwayId);
	}

	public void saveLikedWalkway(Long memberId, Long walkwayId) {
		walkwayRepository.saveLikedWalkway(memberId, walkwayId);
	}

	public void deleteLikedWalkway(Long memberId, Long walkwayId) {
		walkwayRepository.deleteLikedWalkway(memberId, walkwayId);
	}

	public Long saveWalkwayHistory(CreateWalkwayHistory createWalkwayHistory) {
		return walkwayRepository.saveWalkwayHistory(createWalkwayHistory);
	}

	public void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed) {
		walkwayRepository.updateWalkwayHistoryIsReviewed(walkwayHistoryId, isReviewed);
	}

	public void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId) {
		walkwayRepository.updateWalkwayRating(reviewCount, rating, walkwayId);
	}
}
