package com.dongsan.domain.domains.crew.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PRIVATE")
public class PrivateCrew extends Crew {
    private String password;
}
