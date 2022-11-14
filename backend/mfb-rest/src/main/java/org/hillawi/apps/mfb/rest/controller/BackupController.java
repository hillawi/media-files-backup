package org.hillawi.apps.mfb.rest.controller;

import lombok.RequiredArgsConstructor;
import org.hillawi.apps.mfb.rest.domain.BackupReport;
import org.hillawi.apps.mfb.rest.dto.BackupDto;
import org.hillawi.apps.mfb.rest.dto.BackupReportDto;
import org.hillawi.apps.mfb.rest.service.BackupService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ahmed Hillawi
 * @since 13/11/22
 */
@RestController
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @PostMapping(value = "/launchBackup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BackupReportDto launchBackup(@RequestBody BackupDto backupDto) {
        BackupReport backupReport = backupService.execute(backupDto.sourceDeviceId(), backupDto.targetDeviceId(), backupDto.mediaType());
        return new BackupReportDto(backupReport.processedFiles(), backupReport.latestUpdateDate(), backupReport.erroredFiles());
    }

}
