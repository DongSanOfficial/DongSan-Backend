package com.dongsan.api.config;

import com.dongsan.api.domains.auth.security.oauth2.SocialType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"local", "dev"})
@Configuration
public class SwaggerConfig {

    private final String SOCIAL_TAG_NAME = "소셜 로그인";
    @Value("${backend.base-url}")
    private String backendBaseURL;

    @Bean
    public OpenAPI OpenApiConfig() {
        // Servers 에 표시되는 정보 설정
        Server server = new Server();
        server.setUrl(backendBaseURL);
        server.setDescription("동산 Server API");

        OpenAPI openApi = new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 기본적으로 모든 엔드포인트에 대한 JWT 인증이 필요한 것으로 설정
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .info(getInfo())
                // 서버 정보 추가
                .servers(List.of(server))
                .tags(List.of(
                        new Tag().name(SOCIAL_TAG_NAME).description("Oauth2 Endpoint"),
                        new Tag().name("개발용 API").description("Develop API"),
                        new Tag().name("산책로").description("Walkway"),
                        new Tag().name("산책로 리뷰").description("Review of Walkway"),
                        new Tag().name("북마크").description("Bookmark"),
                        new Tag().name("마이페이지").description("Information for User")))
                .path("/oauth2/authorization/kakao", oauth2PathItem(SocialType.KAKAO))
                .path("/oauth2/authorization/naver", oauth2PathItem(SocialType.NAVER))
                ;

        return openApi;
    }


    // 소셜 로그인
    private PathItem oauth2PathItem(SocialType socialLoginType) {
        String socialId = socialLoginType.getRegistrationId();
        return new PathItem().get(new Operation()
                .tags(List.of(SOCIAL_TAG_NAME))
                .summary(socialId)
                // 인증 비활성화
                .security(List.of())
                .description(String.format("[%s](%s/oauth2/authorization/%s)", socialId, backendBaseURL, socialId))
                .responses(new ApiResponses()
                        .addApiResponse("302", new ApiResponse()
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                .type("object")
                                                .example(Map.of(
                                                        "Set-Cookie",
                                                        "accessToken=; Max-Age=3600; Path=/; Domain=dongsanwalk.site; HttpOnly=false; Secure=true, refreshToken=; Max-Age=3600; Path=/; Domain=dongsanwalk.site; HttpOnly=false; Secure=true"
                                                )
                                                )))
                                ))));
    }

    private Info getInfo() {
        return new Info()
                .title("동산 Server API")
                .description("동산 Server API 명세서입니다.")
                .version("v1.0.0");
    }
}
