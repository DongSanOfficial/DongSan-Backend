package com.dongsan.domain.domains.cowalk.domain;

import static com.dongsan.domain.support.error.CoreErrorCode.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.dongsan.domain.domains.common.BaseEntity;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.support.error.CoreException;

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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long crewId;

	private Long memberId;

	private LocalDate date;

	private LocalTime time;

	private Integer capacity;

	@Enumerated(EnumType.STRING)
	private CapacityType capacityType;

	protected CowalkPost() {
	}

	public CowalkPost(CreateCowalkPostCommand command) {
		this.crewId = command.crewId();
		this.memberId = command.memberId();
		this.date = command.date();
		this.time = command.time();
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
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public CapacityType getCapacityType() {
		return capacityType;
	}
}
