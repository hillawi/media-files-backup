package org.hillawi.apps.mfb.rest.dto;

import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record BackupDto(DeviceMediaType mediaType, String sourceDeviceId, String targetDeviceId) {
}
