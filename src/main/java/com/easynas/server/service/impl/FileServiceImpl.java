package com.easynas.server.service.impl;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liangyongrui
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final ConfigDao configDao;

    @Autowired
    public FileServiceImpl(ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public Optional<String> getFilePath(String fileName) {
        return configDao.getFileSavePaths().stream()
                .map(File::new)
                .map(File::listFiles)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .filter(son -> fileName.equals(son.getName()))
                .map(File::getAbsolutePath)
                .findFirst();
    }

    @Override
    public String saveFile(String path, String fileName) {
        return getFilePath(fileName).orElseGet(() -> saveNewFile(path, fileName));
    }

    /**
     * 保存一个保存路径中没有的新文件
     */
    private String saveNewFile(String path, String fileName) {
        final var directoryPath = configDao.getMaxFreeSpacePath();
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
