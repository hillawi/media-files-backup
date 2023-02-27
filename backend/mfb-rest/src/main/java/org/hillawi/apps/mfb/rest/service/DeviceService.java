package org.hillawi.apps.mfb.rest.service;

import org.hillawi.apps.mfb.rest.domain.SourceDevice;
import org.hillawi.apps.mfb.rest.domain.TargetDevice;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public interface DeviceService {

    SourceDevice findSourceDeviceById(String deviceId);

    TargetDevice findTargetDeviceById(String deviceId);

}
