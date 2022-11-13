package org.hillawi.apps.mfb.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
public record DeviceUuid(@JsonProperty("idVendor") String vendorId, @JsonProperty("idProduct") String productId,
                         String fs) {
}