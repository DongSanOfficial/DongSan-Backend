package com.dongsan.rdb.domains.walkway.factory;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
