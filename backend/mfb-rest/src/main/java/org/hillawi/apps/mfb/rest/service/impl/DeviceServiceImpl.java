package org.hillawi.apps.mfb.rest.service.impl;

import org.hillawi.apps.mfb.rest.domain.*;
import org.hillawi.apps.mfb.rest.service.DeviceService;
import org.springframework.stereotype.Service;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Override
    public Device findById(String deviceId) {
        return new Device(deviceId, DeviceConnectionType.USB,
                new DeviceUuid("", ""),
                "name",
                "owner",
                DeviceType.SOURCE,
                new DevicePath("", ""),
                new DevicePath("", ""));
    }

}
