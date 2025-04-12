package com.doomdev.admin_blog.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
	  @Bean
	  public OpenAPI openAPI() {
	    var securityScheme =
	        new SecurityScheme()
	            .type(SecurityScheme.Type.HTTP)
	            .name("bearer")
	            .scheme("bearer")
	            .bearerFormat("JWT")
	            .in(SecurityScheme.In.HEADER);
	    var securityComponent =
	        new Components().addSecuritySchemes("Bearer Authentication", securityScheme);
	    return new OpenAPI()
	        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
	        .components(securityComponent)
	        .info(
	            new Info()
	                .title("Template Service APIs")
	                .description("")
	                .version("v1")
	                .contact(new Contact().name("Team").email("team@ghtk.co").url("team.ghtk.vn")))
	        .servers(
	            List.of(
	                new Server().url("http://127.0.0.1:8080/").description("Local"),
	                new Server().url("https://team.ghtklab.com/").description("Testing"),
	                new Server().url("https://team.ghtk.vn/").description("Production")));
	  }

}
