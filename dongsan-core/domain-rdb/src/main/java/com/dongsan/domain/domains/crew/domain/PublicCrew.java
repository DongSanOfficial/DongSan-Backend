package com.dongsan.domain.domains.crew.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PUBLIC")
public class PublicCrew extends Crew {

}
