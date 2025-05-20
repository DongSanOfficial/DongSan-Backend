package com.dongsan.rdb.domains.crew.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "recruit_comment")
public class RecruitComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recruitPostId;

    private Long memberId;

    private String content;

    protected RecruitComment() {
    }
}
