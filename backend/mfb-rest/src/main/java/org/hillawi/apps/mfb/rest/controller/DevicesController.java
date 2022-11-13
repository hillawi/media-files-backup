package org.hillawi.apps.mfb.rest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@RestController
public class DevicesController {

    @Value("${mfb.devices-url}")
    private FileUrlResource devicesResource;

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource devices() {
        return devicesResource;
    }

}
