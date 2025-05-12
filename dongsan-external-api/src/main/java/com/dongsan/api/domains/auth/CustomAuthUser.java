package com.dongsan.api.domains.auth;

import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.member.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomAuthUser implements OAuth2User, UserDetails {
    private final AuthUserDto user;

    public CustomAuthUser(AuthUserDto user) {
        this.user = user;
    }

    @Override
    public <A> A getAttribute(String name) {
        return (A) getAttributes().get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", user.email(),
                "nickname", user.nickname(),
                "profileImageUrl", user.profileImageUrl(),
                "role", user.role()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> user.role()
                .getDescription());
        return collection;
    }

    @Override
    public String getName() {
        return user.email();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.email();
    }

    public Long getMemberId() {
        return user.memberId();
    }

    public String getEmail() {
        return user.email();
    }

    public String getNickname() {
        return user.nickname();
    }

    public String getProfileImageUrl() {
        return user.profileImageUrl();
    }

    public MemberRole getRole() {
        return user.role();
    }

    public Member getMember() {
        return user.toMember();
    }
}
