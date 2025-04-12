package com.doomdev.admin_blog.controllers;

import com.doomdev.admin_blog.responses.DefaultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealCheckController {
	@Operation(summary = "API health check kiểm tra trạng thái service")
	@SecurityRequirements()
	@GetMapping("/ping")
	public DefaultResponse<String> ping() {
		return DefaultResponse.success("pong");
	}

}
