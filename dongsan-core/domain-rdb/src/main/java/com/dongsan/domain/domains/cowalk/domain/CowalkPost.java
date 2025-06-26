package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.dongsan.domain.support.error.CoreErrorCode.COWALK_PARTICIPANT_LIMIT;

@Entity
@Table(name = "cowalk_post")
public class CowalkPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long memberId;

    private LocalDateTime startedAt;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private CapacityType capacityType;

    protected CowalkPost() {
    }

    public CowalkPost(CreateCowalkPostCommand command) {
        this.crewId = command.crewId();
        this.memberId = command.memberId();
        this.startedAt = LocalDateTime.of(command.date(), command.time());
        this.capacity = command.capacity();
        this.capacityType = command.capacity() == null ? CapacityType.UNLIMITED : CapacityType.LIMITED;
    }

    public void validCapacity(Integer participantCount) {
        if (capacityType.equals(CapacityType.UNLIMITED))
            return;

        if (participantCount >= capacity) {
            throw new CoreException(COWALK_PARTICIPANT_LIMIT);
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

    public Integer getCapacity() {
        return capacity;
    }

    public CapacityType getCapacityType() {
        return capacityType;
    }
}
