package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CrewInfo {
    @Column(nullable = false)
    private String name;

    private String description;

    private String rule;

    private String crewImageUrl;

    protected CrewInfo() {
    }

    public CrewInfo(String name, String description, String rule, String crewImageUrl) {
        validateName(name);
        validateDescription(description);
        validateRule(rule);

        this.name = name;
        this.description = description;
        this.rule = rule;
        this.crewImageUrl = crewImageUrl;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new CoreException(CoreErrorCode.CREW_NAME_NOT_VALID);
        }
    }

    private void validateDescription(String description) {
        if (description != null && !description.isBlank() && description.length() > 250) {
            throw new CoreException(CoreErrorCode.CREW_DESCRIPTION_NOT_VALID);
        }
    }

    private void validateRule(String rule) {
        if (rule != null && !rule.isBlank() && rule.length() > 250) {
            throw new CoreException(CoreErrorCode.CREW_RULE_NOT_VALID);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRule() {
        return rule;
    }

    public String getCrewImageUrl() {
        return crewImageUrl;
    }
}
