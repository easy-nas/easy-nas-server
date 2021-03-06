package com.easynas.server.service;

import com.google.common.io.Files;
import lombok.NonNull;

import java.io.File;
import java.util.*;

/**
 * @author liangyongrui
 */
public interface FileService {

    /**
     * 获取文件保存路径
     *
     * @return 获取文件保存路径
     */
    List<String> getFileSaveRootPaths();

    /**
     * 设置文件保存路径
     *
     * @param path path
     */
    void setFileSavePath(@NonNull final List<String> path);

    /**
     * 得到剩余空间最大的文件保存路径
     *
     * @return path
     */
    default String getMaxFreeSpacePath() {
        return getFileSaveRootPaths().stream().max((o1, o2) -> {
            final var sub = new File(o1).getFreeSpace() - new File(o2).getFreeSpace();
            return sub == 0L ? 0 : (sub > 0 ? 1 : -1);
        }).orElseThrow();
    }

    /**
     * 获取全部文件路径
     *
     * @return { key 文件名, value 文件路径 }
     */
    Map<String, String> getAllFilePath();

    /**
     * 根据文件名得到文件保存路径
     *
     * @param fileName 文件名
     * @return 文件保存路径
     */
    default Optional<String> getFilePath(@NonNull final String fileName) {
        return getFileSaveRootPaths().stream()
                .map(File::new)
                .map(File::listFiles)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .filter(son -> fileName.equals(son.getName()))
                .map(File::getAbsolutePath)
                .map(Files::simplifyPath)
                .findFirst();
    }

    /**
     * 把路径中的文件保存起来，起名叫fileName
     * 如果 fileName 已存在，就直接返回路径
     *
     * @param fileName 文件名
     * @param path 需要保存的文件路径
     * @return 保存地址的路径
     */
    String saveFile(@NonNull final String fileName, @NonNull final String path);
}
