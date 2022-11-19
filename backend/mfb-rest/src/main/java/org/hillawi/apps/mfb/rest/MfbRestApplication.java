package org.hillawi.apps.mfb.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "org.hillawi.apps.mfb.rest.config",
        "org.hillawi.apps.mfb.rest.controller",
        "org.hillawi.apps.mfb.rest.service.impl",
        "org.hillawi.apps.mfb.rest.advice"
}, exclude = ErrorMvcAutoConfiguration.class)
public class MfbRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfbRestApplication.class, args);
    }

}