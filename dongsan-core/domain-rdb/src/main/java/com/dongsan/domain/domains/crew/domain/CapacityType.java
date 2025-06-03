package com.dongsan.domain.domains.crew.domain;

public enum CapacityType {
    LIMITED,
    UNLIMITED;

    public boolean isMemberLimited() {
        return this.equals(LIMITED);
    }
}
