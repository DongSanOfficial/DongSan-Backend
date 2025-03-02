package com.dongsan.api.domains.auth.security.oauth2;

import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private final Member member;

    public CustomOAuth2User(Member member) {
        this.member = member;
    }

    @Override
    public <A> A getAttribute(String name) {
        return (A) getAttributes().get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", member.email(),
                "nickname", member.nickname(),
                "profileImageUrl", member.profileImageUrl(),
                "role", member.role()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> member.role()
                .getDescription());
        return collection;
    }

    @Override
    public String getName() {
        return member.email();
    }

    public Long getMemberId(){
        return member.id();
    }

    public String getEmail(){
        return member.email();
    }

    public String getNickname(){
        return member.nickname();
    }

    public String getProfileImageUrl(){
        return member.profileImageUrl();
    }

    public MemberRole getRole(){
        return member.role();
    }
}
