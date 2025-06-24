package com.dongsan.socket.authenticate;

import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberRdbService;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class SocketJwtService {

    private static final Logger log = LoggerFactory.getLogger(SocketJwtService.class);

    private final MemberRdbService memberRdbService;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;
    private SecretKey accessTokenSecretKey;

    public SocketJwtService(MemberRdbService memberRdbService) {
        this.memberRdbService = memberRdbService;
    }

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
    }

    public boolean isAccessTokenExpired(String accessToken) {
        if (accessToken == null)
            return true;
        long remainingTime = getRemainingTimeMillis(accessToken, accessTokenSecretKey);
        return remainingTime == 0;
    }

    private long getRemainingTimeMillis(String token, SecretKey secretKey) {
        try {
            Date expiration = extractAll(token, secretKey).getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();
            return Math.max(remainingTime, 0);
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.info("[AUTH_INFO] JWT 토큰이 만료: {}", e.getMessage());
            } else {
                log.error("[AUTH_ERROR] JWT 토큰 만료 검사중 알 수 없는 오류 발생: {}", e.getMessage());
            }
            return 0;
        }
    }

    public Member getMemberFromAccessToken(String accessToken) {
        Long memberId = extractAll(accessToken, accessTokenSecretKey)
                .get("memberId", Long.class);
        log.info("websocket 인증 사용자 ID : {}", memberId);
        return memberRdbService.getOptionalMember(memberId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.MEMBER_NOT_FOUND));
    }

    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
