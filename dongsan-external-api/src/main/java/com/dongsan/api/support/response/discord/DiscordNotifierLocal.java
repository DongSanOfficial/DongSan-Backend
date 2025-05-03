package com.dongsan.api.support.response.discord;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class DiscordNotifierLocal implements DiscordNotifier {
    @Override
    public void notify(Exception e, HttpServletRequest request) {

    }
}
