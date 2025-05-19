package com.dongsan.rdb.domains.crew.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "recruit_post")
public class RecruitPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long memberId;

    private LocalDate date;

    private LocalTime time;

    private Integer limit;

    protected RecruitPost() {
    }
}
