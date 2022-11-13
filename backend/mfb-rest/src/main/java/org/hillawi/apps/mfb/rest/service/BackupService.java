package org.hillawi.apps.mfb.rest.service;

import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
public interface BackupService {

    void execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType);

}
