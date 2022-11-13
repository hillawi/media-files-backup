package org.hillawi.apps.mfb.rest.domain;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record Device(String id, DeviceConnectionType deviceConnectionType, DeviceUuid deviceUuid,
                     String name, String owner, DeviceType deviceType, DevicePath mountPath,
                     DevicePath targetSubPath) {
}