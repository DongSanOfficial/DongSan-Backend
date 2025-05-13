package com.dongsan.rdb.domains.bookmark.domain;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "bookmark")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Column(nullable = false)
    private String name;

    protected Bookmark() {
    }

    public Bookmark(String name, Long memberId) {
        this.name = name;
        // 연관관계 매핑
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rename(String name, Long memberId) {
        validateOwner(memberId);
        this.name = name;
    }

    public void validateOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new CoreException(CoreErrorCode.NOT_BOOKMARK_OWNER);
        }
    }
}
