package com.dongsan.api.domains.auth.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleOAuthField {
    @Value("${apple.path}")
    private String path;
    @Value("${apple.url}")
    private String url;
    @Value("${apple.cid}")
    private String cid;
    @Value("${apple.tid}")
    private String tid;
    @Value("${apple.kid}")
    private String kid;

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getCid() {
        return cid;
    }

    public String getTid() {
        return tid;
    }

    public String getKid() {
        return kid;
    }
}
