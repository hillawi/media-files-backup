package org.hillawi.apps.mfb.rest.domain;

import java.util.List;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
public record BackupReport(List<MediaFileDetails> processedFiles, String latestUpdateDate, List<MediaFileDetails> erroredFiles) {
}
