package com.easynas.server.service.impl;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import static java.util.stream.Collectors.toMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liangyongrui
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Bean("fileService")
    @Autowired
    public FileService getFileService(@Qualifier("configDao") @NonNull ConfigDao configDao) {
        return new FileServiceImpl(configDao);
    }

    @Bean("fileBackupService")
    @Autowired
    public FileService getFileBackupService(@Qualifier("configBackupDao") @NonNull ConfigDao configDao) {
        return new FileServiceImpl(configDao);
    }

    private final ConfigDao configDao;

    protected FileServiceImpl(@NonNull ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public Map<String, String> getAllFilePath() {
        return getAllFilePath(getFileSaveRootPaths());
    }

    @Override
    public String saveFile(@NonNull String fileName, @NonNull String path) {
        return getFilePath(fileName).orElseGet(() -> saveNewFile(fileName, path));
    }

    @Override
    public List<String> getFileSaveRootPaths() {
        return configDao.getFileSavePaths();
    }

    @Override
    public void setFileSavePath(@NonNull List<String> path) {
        configDao.setFileSavePath(path);
    }

    /**
     * 得到root下面的所有文件路径
     */
    private Map<String, String> getAllFilePath(@NonNull List<String> root) {
        return root.stream()
                .map(File::new)
                .map(File::listFiles)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .filter(File::isFile)
                .collect(toMap(File::getName, File::getAbsolutePath));
    }

    /**
     * 保存一个保存路径中没有的新文件
     */
    private String saveNewFile(@NonNull String fileName, @NonNull String path) {
        final var directoryPath = getMaxFreeSpacePath();
        final var toPath = directoryPath + "/" + fileName;
        try {
            final Process exec = CommandUtils.cp(path, toPath);
            exec.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("保存文件失败", e);
        }
        return toPath;
    }

}
