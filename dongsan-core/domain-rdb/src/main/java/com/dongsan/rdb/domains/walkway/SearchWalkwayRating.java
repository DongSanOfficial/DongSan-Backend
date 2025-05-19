package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SearchWalkwayRating implements SearchWalkway {
    private final WalkwayRepository walkwayRepository;

    public SearchWalkwayRating(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.RATING;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayRepository.searchWalkwaysRating(searchWalkwayQuery);
    }
}
