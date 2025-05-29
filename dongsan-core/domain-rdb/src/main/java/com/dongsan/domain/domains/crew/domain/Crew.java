package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.*;

@Entity
@Table(name = "crew")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "crew_type")
public abstract class Crew extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String rule;

    private String crewImageUrl;

    @Embedded
    private Capacity capacity;

    protected Crew() {
    }

    public Crew(String name, String description, String rule, String crewImageUrl, Capacity capacity) {
        validateName(name);
        validateDescription(description);
        validateRule(rule);

        this.name = name;
        this.description = description;
        this.rule = rule;
        this.crewImageUrl = crewImageUrl;
        this.capacity = capacity;
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

}
