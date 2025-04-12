package com.doomdev.admin_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = {"com.doomdev.admin_blog"})
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class QPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(QPortalApplication.class, args);
	}

}
