package com.dongsan.domain.domains.walkway.domain;

import java.util.Arrays;

public enum WalkwayExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개");

    private final String description;

    WalkwayExposeLevel(String description) {
        this.description = description;
    }

    public static WalkwayExposeLevel getExposeLevelByDescription(String description) {
        return Arrays.stream(WalkwayExposeLevel.values())
                .filter(val -> val.description.equals(description))
                .findFirst()
                .orElse(WalkwayExposeLevel.PRIVATE);
    }

    public Boolean toBoolean() {
        return this.description.equals("공개");
    }

    public String getDescription() {
        return description;
    }
}
