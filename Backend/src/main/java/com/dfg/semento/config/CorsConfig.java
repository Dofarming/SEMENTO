package com.dfg.semento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173", "http://localhost:5174", "https://k10s106.p.ssafy.io/", "http://k10s106.p.ssafy.io/")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
			.allowedHeaders("Content-Type")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
