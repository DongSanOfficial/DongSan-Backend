package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.walkway.ListStringConverter;
import jakarta.persistence.*;

import java.util.List;

@Embeddable
public class WalkwayInfo {
    @Column(nullable = false)
    private String name;

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ExposeLevel exposeLevel;

    @Convert(converter = ListStringConverter.class)
    private List<String> hashtags;

    @Column(nullable = false)
    private Double distance;  // km

    @Column(nullable = false)
    private Integer time; // 초
}
