package com.easynas.server.task;

import com.easynas.server.service.FileService;
import static java.util.stream.Collectors.toMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 备份任务
 *
 * @author liangyongrui
 */
@Slf4j
@Component
public class BackupSync {

    private final FileService fileService;
    private final FileService fileBackupService;

    @Autowired
    public BackupSync(@Qualifier("fileService") @NonNull FileService fileService,
                      @Qualifier("fileBackupService") @NonNull FileService fileBackupService) {
        this.fileService = fileService;
        this.fileBackupService = fileBackupService;
    }

    @Scheduled(cron = "0 0,30 * * * ?")
    public void backupSync() {
        log.info("开始备份");
        final var fileSaveRoots = fileBackupService.getFileSaveRootPaths();
        if (fileSaveRoots.stream().noneMatch(Strings::isNotBlank)) {
            log.warn("备份路径为空");
            return;
        }
        final var needBackup = getNeedBackupFiles();
        if (needBackup.isEmpty()) {
            return;
        }
        needBackup.forEach(fileBackupService::saveFile);
    }

    private Map<String, String> getNeedBackupFiles() {
        final var allFilePath = fileService.getAllFilePath();
        final var allFilePathBackup = fileBackupService.getAllFilePath();
        return allFilePath.entrySet().stream()
                .filter(t -> !allFilePathBackup.containsKey(t.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
