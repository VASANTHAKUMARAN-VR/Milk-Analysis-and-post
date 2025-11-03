package com.milk.milkanalysis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Local development
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Render production - multiple possible paths try pannum
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/opt/render/project/src/uploads/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/var/data/uploads/");
    }
}