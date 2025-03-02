package com.dongsan.core.domains.walkway;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchWalkwayLiked implements SearchWalkway {

    private final WalkwayRepository walkwayRepository;

    public SearchWalkwayLiked(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.LIKED;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayRepository.searchWalkwaysLiked(searchWalkwayQuery);
    }
}
