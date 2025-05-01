package com.dongsan.api.support.response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public record DiscordMessage(
	String content,
	List<Embed> embeds
) {
	public record Embed(
		String title,
		String description
	) {
	}

	public static DiscordMessage fromException(Exception e, HttpServletRequest request) {
		String content = "# 🚨 에러 발생 비이이이이사아아아앙";
		String description = "### 🕖 발생 시간\n"
			+ LocalDateTime.now()
			+ "\n"
			+ "### 🔗 요청 URL\n"
			+ createRequestFullPath(request)
			+ "\n"
			+ "### 📄 Stack Trace\n"
			+ "```\n"
			+ getStackTrace(e).substring(0, 1000)
			+ "\n```";

		Embed embed = new Embed("ℹ️ 에러 정보", description);
		return new DiscordMessage(content, List.of(embed));
	}

	private static String createRequestFullPath(HttpServletRequest request) {
		String fullPath = request.getMethod() + " " + request.getRequestURL();

		String queryString = request.getQueryString();
		if (queryString != null) {
			fullPath += "?" + queryString;
		}

		return fullPath;
	}

	private static String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
