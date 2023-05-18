package org.hillawi.apps.mfb.rest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hillawi.apps.mfb.rest.domain.BackupReport;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;
import org.hillawi.apps.mfb.rest.domain.MediaFileDetails;
import org.hillawi.apps.mfb.rest.domain.SourceDevice;
import org.hillawi.apps.mfb.rest.domain.exception.BackupException;
import org.hillawi.apps.mfb.rest.service.BackupService;
import org.hillawi.apps.mfb.rest.service.DeviceService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!dummy")
public class BackupServiceImpl implements BackupService {

    private static final EnumMap<DeviceMediaType, String> PATTERNS_PER_MEDIA_TYPE = new EnumMap<>(DeviceMediaType.class);
    private static final LocalTime MIDNIGHT = LocalTime.of(0, 0);
    private static final LocalDateTime FAR_IN_THE_FUTURE =
            LocalDateTime.of(LocalDate.of(9999, 12, 31), MIDNIGHT);


    static {
        PATTERNS_PER_MEDIA_TYPE.put(DeviceMediaType.IMG, "IMG_*.{jpg,jpeg,JPG,JPEG}");
        PATTERNS_PER_MEDIA_TYPE.put(DeviceMediaType.VID, "VID_*.{mp4,MP4}");
    }

    private final DeviceService deviceService;

    @Override
    public BackupReport execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType,
                                LocalDate startDate, LocalDate endDate) {
        var sourceDevice = deviceService.findSourceDeviceById(sourceDeviceId);
        var targetDevice = deviceService.findTargetDeviceById(targetDeviceId);

        var targetSubPath = Paths.get(targetDevice.mountPath(), determineSubPath(sourceDevice, deviceMediaType));

        var updateStartDate = startDate != null ? LocalDateTime.of(startDate, MIDNIGHT) :
                findLatestUpdateDate(sourceDeviceId, deviceMediaType, targetSubPath);
        var updateEndDate = endDate != null ? LocalDateTime.of(endDate, MIDNIGHT) : FAR_IN_THE_FUTURE;

        // TODO Use end date

        String devicePath = determineSourceDevicePath(sourceDevice, deviceMediaType);
        var filesPaths = findFiles(Paths.get(devicePath), "glob:" + PATTERNS_PER_MEDIA_TYPE.get(deviceMediaType));

        var processedFiles = doBackup(targetSubPath, filesPaths, updateStartDate, updateEndDate);

        if (CollectionUtils.isEmpty(processedFiles)) {
            throw Problem.builder()
                    .withStatus(Status.BAD_REQUEST)
                    .withDetail("Nothing to backup")
                    .build();
        }

        var latestBackupDate = extractDate(processedFiles.get(processedFiles.size() - 1).name());

        updateLatestUpdateFile(latestBackupDate, sourceDeviceId, deviceMediaType, targetSubPath);

        return new BackupReport(processedFiles, latestBackupDate, Collections.emptyList());
    }

    @SneakyThrows
    private List<Path> findFiles(Path rootDir, String pattern) {
        var matchesList = new ArrayList<Path>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attribs) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
                if (matcher.matches(path.getFileName())) {
                    matchesList.add(path);
                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(rootDir, Set.of(FileVisitOption.FOLLOW_LINKS), 1, matcherVisitor);
        } catch (IOException e) {
            log.error("Can't read the source files", e);
            throw new RuntimeException("Source device not readable");
        }
        return matchesList;
    }

    private LocalDateTime findLatestUpdateDate(String sourceDeviceId, DeviceMediaType deviceMediaType, Path targetSubPath) {
        try {
            var latestUpdateFileName = buildLatestUpdateFileName(sourceDeviceId, deviceMediaType);
            var lines = Files.readAllLines(targetSubPath.resolve(latestUpdateFileName));
            // This filed should have only one line
            return LocalDateTime.of(LocalDate.parse(lines.get(0)), MIDNIGHT);
        } catch (IOException e) {
            log.error("Cannot find the latest update file. Aborting.", e);
            throw new BackupException("Cannot find the latest update file. Make sure the source device is connected");
        }
    }

    private ArrayList<MediaFileDetails> doBackup(Path targetPath, List<Path> filePaths,
                                                 LocalDateTime startDateTime, LocalDateTime endDateTime) {
        var processedFiles = new ArrayList<MediaFileDetails>();
        filePaths.stream()
                .filter(p -> {
                    try {
                        FileTime creationTime = (FileTime) Files.getAttribute(p, "creationTime");
                        LocalDateTime creationDateTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneOffset.UTC);
                        return creationDateTime.compareTo(startDateTime) >= 0 && creationDateTime.compareTo(endDateTime) <= 0;
                    } catch (IOException e) {
                        log.error("Can't determine file creation time for file {}", p.getFileName(), e);
                        return false;
                    }
                }).sorted()
                .forEach(p -> {
                    try {
                        var fileName = p.getFileName();
                        var dirName = createDirectoryIfNotExists(fileName, targetPath);
                        Files.copy(p, targetPath.resolve(dirName).resolve(fileName), REPLACE_EXISTING);

                        processedFiles.add(new MediaFileDetails(fileName.toString(), Files.size(p) / 1024));
                    } catch (IOException e) {
                        log.error("Cannot copy file at {}", p, e);
                    }
                });
        return processedFiles;
    }

    private void updateLatestUpdateFile(String latestBackupDate, String sourceDeviceId, DeviceMediaType deviceMediaType, Path targetSubPath) {
        var latestUpdateFileName = buildLatestUpdateFileName(sourceDeviceId, deviceMediaType);
        try {
            Files.writeString(targetSubPath.resolve(latestUpdateFileName), latestBackupDate, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new BackupException("Cannot update the most recent updated file " + latestUpdateFileName, e);
        }
    }

    private Path createDirectoryIfNotExists(Path fileName, Path targetPath) {
        var dirName = extractDirectoryName(fileName);
        Path fullPath = targetPath.resolve(dirName);
        try {
            return Files.createDirectories(fullPath);
        } catch (IOException e) {
            throw new BackupException("Cannot create directory " + fullPath, e);
        }
    }

    private String extractDirectoryName(Path path) {
        return path.toString().substring(4).substring(0, 6);
    }

    private String buildLatestUpdateFileName(String sourceDeviceId, DeviceMediaType deviceMediaType) {
        return "latest_" + sourceDeviceId + "_" + deviceMediaType;
    }

    private String extractDate(String latestProcessedFile) {
        var date = latestProcessedFile.substring(4).substring(0, 8);
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    private static String determineSubPath(SourceDevice sourceDevice, DeviceMediaType deviceMediaType) {
        return switch (deviceMediaType) {
            case IMG -> sourceDevice.targetSubPath().img();
            case VID -> sourceDevice.targetSubPath().vid();
        };
    }

    private String determineSourceDevicePath(SourceDevice sourceDevice, DeviceMediaType deviceMediaType) {
        return switch (deviceMediaType) {
            case IMG -> sourceDevice.mountPath().img();
            case VID -> sourceDevice.mountPath().vid();
        };
    }

}
