package com.dongsan.domain.domains.cowalk.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.dongsan.domain.domains.common.BaseEntity;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cowalk_post")
public class CowalkPost extends BaseEntity {
    private static final long MIN_DURATION_HOUR = 1;
    private static final long MAX_DURATION_HOUR = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private CapacityType capacityType;

    private String memo;

    protected CowalkPost() {
    }

    public CowalkPost(CreateCowalkPostCommand command) {
        this.crewId = command.crewId();
        this.memberId = command.memberId();
        this.startedAt = command.startedAt();
        this.endedAt = command.endedAt();
        this.capacity = command.capacity();
        this.capacityType = command.capacity() == null ? CapacityType.UNLIMITED : CapacityType.LIMITED;
        this.memo = command.memo();
    }

    public void validCapacity(Integer participantCount) {
        if (capacityType.equals(CapacityType.UNLIMITED))
            return;

        if (participantCount >= capacity) {
            throw new CoreException(CoreErrorCode.COWALK_PARTICIPANT_LIMIT);
        }
    }

    public void validateCowalkDuration(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt == null || endedAt == null) {
            throw new CoreException(CoreErrorCode.COWALK_STARTEDAT_ENDEDAT_NOTNULL);
        }

        if (endedAt.isBefore(startedAt)) {
            throw new CoreException(CoreErrorCode.COWALK_STARTEDAT_EARLY_ENDEDAT);
        }

        Duration duration = Duration.between(startedAt, endedAt);
        long durationMinutes = duration.toMinutes();
        if (durationMinutes < MIN_DURATION_HOUR * 60 || durationMinutes > MAX_DURATION_HOUR * 60) {
            throw new CoreException(CoreErrorCode.COWALK_DURATION_BETWEEN_1H_3H);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDate() {
        return startedAt.toLocalDate();
    }

    public LocalTime getTime() {
        return startedAt.toLocalTime();
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public CapacityType getCapacityType() {
        return capacityType;
    }

    public String getMemo() {
        return memo;
    }

    public LocalTime getEndTime() {
        return endedAt.toLocalTime();
    }
}
