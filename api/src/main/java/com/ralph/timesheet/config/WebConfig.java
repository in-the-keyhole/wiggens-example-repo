package com.ralph.timesheet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.api-base:/codex-example/api/v1}")
    private String apiBase;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // no-op; using explicit RequestMapping paths including base
    }
}

