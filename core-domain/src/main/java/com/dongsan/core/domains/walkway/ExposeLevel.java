package com.dongsan.core.domains.walkway;

import java.util.Arrays;

public enum ExposeLevel {
    PRIVATE("비공개"),
    PUBLIC("공개")
    ;

    private final String description;

    ExposeLevel(String description) {
        this.description = description;
    }

    public static ExposeLevel getExposeLevelByDescription(String description) {
        return Arrays.stream(ExposeLevel.values())
                .filter(val -> val.description.equals(description))
                .findFirst()
                .orElse(ExposeLevel.PRIVATE);
    }

    public Boolean toBoolean() {
        return this.description.equals("공개");
    }

    public String getDescription() {
        return description;
    }
}
