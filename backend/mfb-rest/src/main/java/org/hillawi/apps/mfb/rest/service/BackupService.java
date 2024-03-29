package org.hillawi.apps.mfb.rest.service;

import org.hillawi.apps.mfb.rest.domain.BackupReport;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;

import java.time.LocalDate;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
public interface BackupService {

    BackupReport execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType,
                         LocalDate startDate, LocalDate endDate);

}