package com.ryulth.sns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

    private @Value("${allowed.origin}")
    String origin;
    private static final String[] INCLUDE_PATHS = {
            "/timeline/**",
            "/event/**",
            "/events/**",
            "/files/**",
            "/friend/**",
            "/friends/**",
            "/profile/**"
    };

    private static final String[] EXCLUDE_PATHS = {
            "/static/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v2/api-docs",
            "/configuration/security",
            "/configuration/ui",
            "/swagger-resources",
            "/","/csrf"
    };

    private final TokenInterceptor tokenInterceptor;

    public WebConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(INCLUDE_PATHS)
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins(origin)
                .allowCredentials(false)
                .maxAge(3600);
    }
}