package com.dongsan.rds.domains.member;

import com.dongsan.core.domains.auth.Provider;
import com.dongsan.rds.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    //@Column(columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    protected Member() {
    }

    public Member(Long id, String email, String nickname, String profileImageUrl, MemberRole role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    public Member(String email, String nickname, String profileImageUrl, MemberRole role, Provider provider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public MemberRole getRole() {
        return role;
    }
}
