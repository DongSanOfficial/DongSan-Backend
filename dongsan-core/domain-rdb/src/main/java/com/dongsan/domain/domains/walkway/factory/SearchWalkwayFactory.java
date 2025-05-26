package com.dongsan.domain.domains.walkway.factory;

import com.dongsan.domain.domains.walkway.WalkwaySort;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchWalkwayFactory {
    private final Map<WalkwaySort, SearchWalkway> walkwaySearchMap = new EnumMap<>(WalkwaySort.class);

    public SearchWalkwayFactory(List<SearchWalkway> searchWalkways) {
        for (SearchWalkway searchWalkway : searchWalkways) {
            this.walkwaySearchMap.put(searchWalkway.getSortType(), searchWalkway);
        }
    }

    public SearchWalkway getService(WalkwaySort sort) {
        return walkwaySearchMap.get(sort);
    }
}
