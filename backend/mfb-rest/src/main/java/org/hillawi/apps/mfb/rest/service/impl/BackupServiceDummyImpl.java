package org.hillawi.apps.mfb.rest.service.impl;

import lombok.SneakyThrows;
import org.hillawi.apps.mfb.rest.domain.BackupReport;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;
import org.hillawi.apps.mfb.rest.domain.MediaFileDetails;
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

    @SneakyThrows
    @Override
    public BackupReport execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType) {
        Thread.sleep(5000L);
        return new BackupReport(
                List.of(new MediaFileDetails("20221111.jpg", 10),
                        new MediaFileDetails("20221115.jpg", 20),
                        new MediaFileDetails("20221116.jpg", 15),
                        new MediaFileDetails("20221117.jpg", 50)),
                "20221117.jpg",
                List.of(new MediaFileDetails("20221112.jpg", 10), new MediaFileDetails("20221113.jpg", 10)));
    }

}