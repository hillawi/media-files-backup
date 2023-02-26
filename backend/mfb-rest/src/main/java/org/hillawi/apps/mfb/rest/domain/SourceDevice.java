package org.hillawi.apps.mfb.rest.domain;

/**
 * @author Ahmed Hillawi
 * @since 26/02/23
 */
public record SourceDevice(String id, DeviceConnectionType connectionType, DeviceUuid uuid,
                           String name, String owner, DevicePath mountPath,
                           DevicePath targetSubPath) {
}