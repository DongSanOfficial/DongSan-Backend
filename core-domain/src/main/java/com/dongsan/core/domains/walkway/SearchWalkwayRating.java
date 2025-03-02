package com.dongsan.core.domains.walkway;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
