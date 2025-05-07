package com.dongsan.api.support.response.discord;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DiscordNotifierDev implements DiscordNotifier {
    private final DiscordClient discordClient;

    public DiscordNotifierDev(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public void notify(Exception e, HttpServletRequest request) {
        discordClient.sendAlarm(
                DiscordMessage.fromException(e, request)
        );
    }
}
