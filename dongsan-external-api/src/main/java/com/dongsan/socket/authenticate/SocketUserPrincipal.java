package com.dongsan.socket.authenticate;

import com.dongsan.domain.domains.member.Member;

import java.security.Principal;

public class SocketUserPrincipal implements Principal {
    private final Long memberId;
    private final String nickname;

    public SocketUserPrincipal(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
    }

    @Override
    public String getName() {
        return memberId.toString();
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNickname() {
        return nickname;
    }
}
