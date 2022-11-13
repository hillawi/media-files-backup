package org.hillawi.apps.mfb.rest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hillawi.apps.mfb.rest.domain.DeviceMediaType;
import org.hillawi.apps.mfb.rest.service.BackupService;
import org.hillawi.apps.mfb.rest.service.DeviceService;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final DeviceService deviceService;

    @Override
    public void execute(String sourceDeviceId, String targetDeviceId, DeviceMediaType deviceMediaType) {
        var sourceDevice = deviceService.findById(sourceDeviceId);
        var targetDevice = deviceService.findById(targetDeviceId);

        // TODO fix

        var fileNames = searchWithWc(Paths.get(sourceDevice.mountPath() + deviceMediaType.name().toLowerCase() + "/"),
                "glob:" + deviceMediaType + "_*.{jpg,jpeg,JPG,JPEG}");

        fileNames.forEach(log::info);
    }

    @SneakyThrows
    private List<String> searchWithWc(Path rootDir, String pattern) {
        var matchesList = new ArrayList<String>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matchesList.add(name.toString());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(rootDir, matcherVisitor);
        return matchesList;
    }

}
