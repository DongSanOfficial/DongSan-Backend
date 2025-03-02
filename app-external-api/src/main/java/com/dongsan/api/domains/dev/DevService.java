package com.dongsan.api.domains.dev;

import com.dongsan.api.domains.auth.CookieService;
import com.dongsan.api.domains.auth.JwtService;
import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.error.ApiException;
import com.dongsan.core.domains.auth.TokenWriter;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberService;
import com.dongsan.file.service.S3FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DevService {
    private final MemberService memberService;
    private final TokenWriter tokenWriter;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final S3FileService s3FileService;

    public DevService(MemberService memberService, TokenWriter tokenWriter, JwtService jwtService,
                      CookieService cookieService,
                      S3FileService s3FileService) {
        this.memberService = memberService;
        this.tokenWriter = tokenWriter;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.s3FileService = s3FileService;
    }

    public void generateToken(Long memberId, HttpServletResponse response){
        memberService.getMember(memberId);
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        tokenWriter.saveRefreshToken(memberId, refreshToken);
    }

    public GetMemberInfoResponse getMemberInfo(HttpServletRequest request){
        String accessToken = cookieService.getAccessTokenFromCookie(request);
        if(jwtService.isAccessTokenExpired(accessToken)){
            throw new ApiException(ApiErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        return new GetMemberInfoResponse(member);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        return s3FileService.saveFile(image);
    }
}
