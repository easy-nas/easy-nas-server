package com.easynas.server.task;

import com.easynas.server.service.ConfigService;
import com.easynas.server.service.FileService;
import static java.util.stream.Collectors.toMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ConfigService configDao;
    private final FileService fileService;

    @Autowired
    public BackupSync(ConfigService configDao, FileService fileService) {
        this.configDao = configDao;
        this.fileService = fileService;
    }

    @Scheduled(cron = "0 0,30 * * * ?")
    public void backupSync() {
        final var needBackup = getNeedBackupFiles();
        if (needBackup.isEmpty()) {
            return;
        }
        needBackup.forEach(fileService::backupFile);
    }

    private Map<String, String> getNeedBackupFiles() {
        final var allFilePath = configDao.getAllFilePath();
        final var allFilePathBackup = configDao.getAllFilePathBackup();
        return allFilePath.entrySet().stream()
                .filter(t -> !allFilePathBackup.containsKey(t.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
