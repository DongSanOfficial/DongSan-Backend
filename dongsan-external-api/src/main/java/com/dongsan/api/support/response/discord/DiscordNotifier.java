package com.dongsan.api.support.response.discord;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public interface DiscordNotifier {
    void notify(Exception e, HttpServletRequest request);
}
