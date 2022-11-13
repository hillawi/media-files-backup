package org.hillawi.apps.mfb.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${mfb.frontend.url}")
    private String frontendUrl;

    @Value("${mfb.cors.allowed-headers}")
    private String[] corsAllowedHeaders;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders(corsAllowedHeaders)
                .allowCredentials(true)
                .maxAge(5000L);
    }

}