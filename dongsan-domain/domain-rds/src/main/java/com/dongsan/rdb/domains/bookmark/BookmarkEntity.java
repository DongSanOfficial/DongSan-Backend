package com.dongsan.rdb.domains.bookmark;

import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.support.util.Author;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookmark")
public class BookmarkEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;  // 북마크 생성자

	@Column(nullable = false)
	private String name;

	protected BookmarkEntity() {
	}

	public BookmarkEntity(String name, MemberEntity member) {
		this.name = name;
		// 연관관계 매핑
		this.member = member;
	}

	public Long getId() {
		return id;
	}

	public void rename(String name) {
		this.name = name;
	}

	public Bookmark toBookmark() {
		return new Bookmark(id, name, new Author(member.getId()), getCreatedAt());
	}

	public MemberEntity getMember() {
		return member;
	}

	public String getName() {
		return name;
	}
}
