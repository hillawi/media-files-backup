package org.hillawi.apps.mfb.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.hillawi.apps.mfb.rest.config",
        "org.hillawi.apps.mfb.rest.controller",
        "org.hillawi.apps.mfb.rest.service.impl"
})
public class MfbRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfbRestApplication.class, args);
    }

}