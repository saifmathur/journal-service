package com.journal.journal_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow CORS for all paths
                .allowedOrigins("http://localhost:4200","https://journal-"+System.getenv("VERCEL_APP_ID")+"-saifmathurs-projects.vercel.app/","https://journal-taupe-theta.vercel.app")  // Allow your Angular app origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allowed HTTP methods
                .allowedHeaders("Authorization", "Content-Type")  // Allow all headers
                .allowCredentials(true);  // Allow sending credentials (cookies, authorization headers)
    }
}

