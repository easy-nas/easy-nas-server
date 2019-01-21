package com.easynas.server.service;

import java.util.Optional;

/**
 * @author liangyongrui
 */
public interface FileService {
    /**
     * 根据文件名得到文件保存路径
     *
     * @param fileName 文件名
     * @return 文件保存路径
     */
    Optional<String> getFilePath(String fileName);

    /**
     * 把路径中的文件保存起来，起名叫fileName
     * 如果 fileName 已存在，就直接返回路径
     *
     * @param path     需要保存的文件路径
     * @param fileName 文件名
     * @return 保存的地址的绝对路径
     */
    String saveFile(String path, String fileName);
}
