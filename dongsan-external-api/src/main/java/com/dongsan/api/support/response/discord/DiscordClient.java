package com.dongsan.api.support.response.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = "discord-client",
	url = "${discord.webhook.url}")
@Profile("dev")
public interface DiscordClient {
	@PostMapping()
	void sendAlarm(@RequestBody DiscordMessage message);
}
