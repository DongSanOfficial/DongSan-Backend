package com.dongsan.domain.domains.walkway.domain;

public enum WalkwayExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개");

    private final String description;

    WalkwayExposeLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
