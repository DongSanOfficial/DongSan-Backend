package com.dongsan.domain.domains.member;

public enum MemberRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ;

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
