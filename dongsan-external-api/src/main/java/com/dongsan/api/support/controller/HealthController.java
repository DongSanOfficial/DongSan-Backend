package com.dongsan.api.support.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "개발용 API")
public class HealthController {

	@Operation(summary = "애플리케이션 헬스체크")
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Server is Healthy!");
	}

	@Operation(summary = "홈 헬스체크")
	@GetMapping("/")
	public ResponseEntity<String> home() {
		return ResponseEntity.ok("It's Home!");
	}
}
