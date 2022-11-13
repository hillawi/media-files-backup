package org.hillawi.apps.mfb.rest.controller;

import lombok.RequiredArgsConstructor;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;
import org.hillawi.apps.mfb.rest.service.DeviceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@RestController
@RequiredArgsConstructor
public class BackupController {

    private DeviceService deviceService;

    @PostMapping(value = "/launchBackup", produces = MediaType.APPLICATION_JSON_VALUE)
    public String launchBackup(DeviceMediaType mediaType, String sourceDeviceId, String targetDeviceId) {
        var sourceDevice = deviceService.findById(sourceDeviceId);
        var targetDevice = deviceService.findById(targetDeviceId);
        return "OK";
    }

}
