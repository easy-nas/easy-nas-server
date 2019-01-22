package com.easynas.server.service.base;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.FileService;
import com.easynas.server.util.CommandUtils;
import static java.util.stream.Collectors.toMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午4:39
 */
@Slf4j
public abstract class BaseFileService implements FileService {

    protected ConfigDao configDao;

    protected BaseFileService(@NonNull ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public Map<String, String> getAllFilePath() {
        return getAllFilePath(getFileSavePaths());
    }

    /**
     * 得到root下面的所有文件路径
     */
    protected Map<String, String> getAllFilePath(@NonNull List<String> root) {
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
    protected String saveNewFile(String fileName, String path) {
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
