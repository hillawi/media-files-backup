package org.hillawi.apps.mfb.rest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hillawi.apps.mfb.rest.domain.Device;
import org.hillawi.apps.mfb.rest.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Value("${mfb.devices-url}")
    private FileUrlResource devicesResource;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Device findById(String deviceId) {
        Device[] devices = objectMapper.readValue(devicesResource.getURL(), Device[].class);
        return Arrays.stream(devices).filter(d -> d.id().equals(deviceId)).findFirst().get();
    }

}