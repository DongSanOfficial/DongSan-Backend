package com.dongsan.rdb.domains.crew.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "recruit_participant")
public class RecruitParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recruitPostId;

    private Long memberId;

    protected RecruitParticipant() {
    }
}
