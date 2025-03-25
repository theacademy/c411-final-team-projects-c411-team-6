package org.mthree.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*") // Allow all endpoints
                .allowedOrigins("http://localhost:3000") // Allow your frontend origin
                .allowedMethods("*") // Allow all methods (GET, POST, etc)
                .allowedHeaders("*"); // Allow all headers
    }
}
