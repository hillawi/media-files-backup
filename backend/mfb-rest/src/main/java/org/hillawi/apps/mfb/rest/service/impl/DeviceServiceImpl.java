package org.hillawi.apps.mfb.rest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hillawi.apps.mfb.rest.domain.SourceDevice;
import org.hillawi.apps.mfb.rest.domain.TargetDevice;
import org.hillawi.apps.mfb.rest.service.DeviceService;
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

    private final FileUrlResource sourceDevicesResource;
    private final FileUrlResource targetDevicesResource;
    private final ObjectMapper objectMapper;

    public DeviceServiceImpl(@Value("${mfb.devices.sources-url}") FileUrlResource sourceDevicesResource,
                             @Value("${mfb.devices.targets-url}") FileUrlResource targetDevicesResource,
                             ObjectMapper objectMapper) {
        this.sourceDevicesResource = sourceDevicesResource;
        this.targetDevicesResource = targetDevicesResource;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public SourceDevice findSourceDeviceById(String deviceId) {
        SourceDevice[] sourceDevices = objectMapper.readValue(sourceDevicesResource.getURL(), SourceDevice[].class);
        return Arrays.stream(sourceDevices).filter(d -> d.id().equals(deviceId)).findFirst().orElseThrow();
    }

    @Override
    @SneakyThrows
    public TargetDevice findTargetDeviceById(String deviceId) {
        TargetDevice[] targetDevices = objectMapper.readValue(targetDevicesResource.getURL(), TargetDevice[].class);
        return Arrays.stream(targetDevices).filter(d -> d.id().equals(deviceId)).findFirst().orElseThrow();
    }

}