package org.hillawi.apps.mfb.rest.service.impl;

import org.hillawi.apps.mfb.rest.domain.BackupReport;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;
import org.hillawi.apps.mfb.rest.service.BackupService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ahmed Hillawi
 * @since 15/11/22
 */
@Service
@Profile("dummy")
public class BackupServiceDummyImpl implements BackupService {

    @Override
    public BackupReport execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType) {
        return new BackupReport(
                List.of("20221111.jpg", "20221115.jpg", "20221116.jpg", "20221117.jpg"),
                "20221117.jpg",
                List.of("20221112.jpg", "20221113.jpg", "20221114.jpg"));
    }

}