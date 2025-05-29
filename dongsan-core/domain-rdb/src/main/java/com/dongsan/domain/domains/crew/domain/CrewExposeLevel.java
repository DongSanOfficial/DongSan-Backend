package com.dongsan.domain.domains.crew.domain;

public enum CrewExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개");

    private final String description;

    CrewExposeLevel(String description) {
        this.description = description;
    }
}
