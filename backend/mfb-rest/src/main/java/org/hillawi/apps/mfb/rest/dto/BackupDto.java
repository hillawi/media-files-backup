package org.hillawi.apps.mfb.rest.dto;

import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;

import java.time.LocalDate;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record BackupDto(DeviceMediaType mediaType, String sourceDeviceId, String targetDeviceId, LocalDate startDate) {
}
