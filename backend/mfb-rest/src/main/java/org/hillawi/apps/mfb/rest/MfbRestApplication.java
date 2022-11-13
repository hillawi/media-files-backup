package org.hillawi.apps.mfb.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MfbRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfbRestApplication.class, args);
    }

}

@RestController
class DevicesController {

    @Value("${mfb.devices-url}")
    FileUrlResource devicesResource;

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource devices() {
        return devicesResource;
    }

}