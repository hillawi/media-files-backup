package org.hillawi.apps.mfb.rest.service;

import org.hillawi.apps.mfb.rest.domain.Device;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public interface DeviceService {

    Device findById(String deviceId);

}
