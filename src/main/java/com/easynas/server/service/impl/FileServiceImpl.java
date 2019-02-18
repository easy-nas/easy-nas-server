package com.easynas.server.service.impl;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import com.google.common.io.Files;
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

import static java.util.stream.Collectors.toMap;

/**
 * @author liangyongrui
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Bean("fileService")
    @Autowired
    public FileService getFileService(@Qualifier("configDao") @NonNull final ConfigDao configDao) {
        return new FileServiceImpl(configDao);
    }

    @Bean("fileBackupService")
    @Autowired
    public FileService getFileBackupService(@Qualifier("configBackupDao") @NonNull final ConfigDao configDao) {
        return new FileServiceImpl(configDao);
    }

    private final ConfigDao configDao;

    protected FileServiceImpl(@NonNull final ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public Map<String, String> getAllFilePath() {
        return getAllFilePath(getFileSaveRootPaths());
    }

    @Override
    public String saveFile(@NonNull final String fileName, @NonNull final String path) {
        return getFilePath(fileName).orElseGet(() -> saveNewFile(fileName, path));
    }

    @Override
    public List<String> getFileSaveRootPaths() {
        return configDao.getFileSavePaths();
    }

    @Override
    public void setFileSavePath(@NonNull final List<String> path) {
        configDao.setFileSavePath(path);
    }

    /**
     * 得到root下面的所有文件路径
     */
    private Map<String, String> getAllFilePath(@NonNull final List<String> root) {
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
    private String saveNewFile(@NonNull final String fileName, @NonNull final String path) {
        final var directoryPath = getMaxFreeSpacePath();
        final var toPath = Files.simplifyPath(directoryPath + File.separator + fileName);
        try {
            final var exec = CommandUtils.cp(path, toPath);
            exec.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("保存文件失败", e);
        }
        return toPath;
    }

}
