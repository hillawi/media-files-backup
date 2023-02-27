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

    private final FileUrlResource sourceDevicesResource;
    private final FileUrlResource targetDevicesResource;

    public DevicesController(@Value("${mfb.devices.sources-url}") FileUrlResource sourceDevicesResource,
                             @Value("${mfb.devices.targets-url}") FileUrlResource targetDevicesResource) {
        this.sourceDevicesResource = sourceDevicesResource;
        this.targetDevicesResource = targetDevicesResource;
    }

    @GetMapping(value = "/devices/sources", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource sourceDevices() {
        return sourceDevicesResource;
    }

    @GetMapping(value = "/devices/targets", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource targetDevices() {
        return targetDevicesResource;
    }

}
