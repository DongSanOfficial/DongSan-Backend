package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "liked_walkway")
public class LikedWalkwayEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walkway_id")
	private WalkwayEntity walkway;

	protected LikedWalkwayEntity() {
	}

	// 생성 시 연관관계 매핑 진행
	public LikedWalkwayEntity(MemberEntity member, WalkwayEntity walkway) {
		this.member = member;
		this.walkway = walkway;
	}

	public Long getId() {
		return id;
	}

	public MemberEntity getMemberEntity() {
		return member;
	}

	public WalkwayEntity getWalkwayEntity() {
		return walkway;
	}
}
