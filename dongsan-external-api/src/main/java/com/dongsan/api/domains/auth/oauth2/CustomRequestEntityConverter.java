package com.dongsan.api.domains.auth.oauth2;

import io.jsonwebtoken.Jwts;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private static final Logger log = LoggerFactory.getLogger(CustomRequestEntityConverter.class);
    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;
    private final String path;
    private final String keyId;
    private final String teamId;
    private final String clientId;
    private final String url;

    public CustomRequestEntityConverter(AppleOAuthField appleOAuthField) {
        this.defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
        this.path = appleOAuthField.getPath();
        this.keyId = appleOAuthField.getKid();
        this.teamId = appleOAuthField.getTid();
        this.clientId = appleOAuthField.getCid();
        this.url = appleOAuthField.getUrl();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
        RequestEntity<?> entity = defaultConverter.convert(req);
        String registrationId = req.getClientRegistration().getRegistrationId();
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();

        if (registrationId.contains("apple")) {
            try {
                params.set("client_secret", createClientSecret());
            } catch (IOException e) {
                log.error("[Apple OAuth2] client_secret 생성 실패", e);
                throw new RuntimeException(e);
            }
        }
        log.info("[Apple OAuth2] 최종 OAuth2 요청 파라미터: {}", params);

        return new RequestEntity<>(params, entity.getHeaders(),
                entity.getMethod(), entity.getUrl());
    }

    public PrivateKey getPrivateKey() throws IOException {
        log.info("[Apple OAuth2] private key 파일 읽기 시작 - 경로: {}", path);

        File file = new File(path);
        if (!file.exists()) {
            log.error("[Apple OAuth2] private key 파일이 존재하지 않음: {}", path);
            throw new FileNotFoundException("Private key not found at " + path);
        }

        try (InputStream in = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }

            PEMParser pemParser = new PEMParser(new StringReader(sb.toString()));
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            log.info("[Apple OAuth2] private key 파싱 완료");
            return converter.getPrivateKey(object);
        }
    }

    public String createClientSecret() throws IOException {
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", keyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .header().add(jwtHeader).and()
                .issuer(teamId)
                .issuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))// 만료 시간
                .audience().add(url).and()
                .subject(clientId)
                .signWith(getPrivateKey())  //.signWith(getPrivateKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
