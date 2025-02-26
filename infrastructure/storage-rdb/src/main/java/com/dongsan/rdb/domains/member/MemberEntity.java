package com.dongsan.rdb.domains.member;

import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    protected MemberEntity(){}

    public MemberEntity(String email, String nickname, String profileImageUrl, MemberRole role){
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Member toMember(){
        return new Member(id, email, nickname, profileImageUrl, role);
    }
}
