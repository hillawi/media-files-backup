package org.hillawi.apps.mfb.rest.domain;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record Device(String id, DeviceConnectionType connectionType, DeviceUuid uuid,
                     String name, String owner, DeviceType type, String mountPath,
                     DevicePath targetSubPath) {
}