package com.dongsan.rdb.domains.crew.domain;


import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "crew")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "crew_type")
public abstract class Crew extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String rule;

    private Integer limit;

    protected Crew() {
    }
}
