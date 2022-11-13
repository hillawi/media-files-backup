package org.hillawi.apps.mfb.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class MfbConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfbConfigApplication.class, args);
    }

}
