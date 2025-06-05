package com.dongsan.domain.domains.crew.domain;

public enum CapacityType {
    LIMITED,
    UNLIMITED;

    public boolean isLimitedCrew() {
        return this.equals(LIMITED);
    }
}
