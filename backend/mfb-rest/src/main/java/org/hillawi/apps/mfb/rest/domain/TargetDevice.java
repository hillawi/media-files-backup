package org.hillawi.apps.mfb.rest.domain;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record TargetDevice(String id, DeviceUuid uuid, String name, String owner, String mountPath) {
}