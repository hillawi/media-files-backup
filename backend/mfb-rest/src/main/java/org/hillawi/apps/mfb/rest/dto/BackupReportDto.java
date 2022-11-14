package org.hillawi.apps.mfb.rest.dto;

import java.util.List;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
public record BackupReportDto(List<String> processedFiles, String latestUpdateDate, List<String> erroredFiles) {
}
