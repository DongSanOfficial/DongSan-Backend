package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Capacity {
    private static final int MIN_COUNT = 2;
    private static final int MAX_COUNT = 100;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private CapacityType capacityType;

    private Integer memberLimit;

    protected Capacity() {
    }

    public Capacity(boolean limitEnable, Integer memberLimit) {
        if (limitEnable) {
            validateMemberLimit(memberLimit);
            this.capacityType = CapacityType.LIMITED;
            this.memberLimit = memberLimit;
        } else {
            this.capacityType = CapacityType.UNLIMITED;
        }
    }

    private void validateMemberLimit(Integer memberLimit) {
        if (memberLimit == null || memberLimit < MIN_COUNT || memberLimit > MAX_COUNT) {
            throw new CoreException(CoreErrorCode.CREW_MEMBER_LIMIT_NOT_VALID);
        }
    }

}
