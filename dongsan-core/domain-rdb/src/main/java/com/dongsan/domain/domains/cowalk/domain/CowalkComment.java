package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.domains.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cowalk_comment")
public class CowalkComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cowalkPostId;

    private Long memberId;

    private String content;

    protected CowalkComment() {
    }
}
