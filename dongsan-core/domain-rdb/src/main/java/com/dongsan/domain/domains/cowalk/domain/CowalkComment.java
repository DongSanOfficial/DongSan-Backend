package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

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
