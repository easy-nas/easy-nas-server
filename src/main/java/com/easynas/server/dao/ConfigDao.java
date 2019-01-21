package com.easynas.server.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author liangyongrui
 */
public interface ConfigDao {

    /**
     * 获取通用信息保存路径
     *
     * @return 获取通用信息保存路径
     */
    String getGeneralInformationPath();

    /**
     * 设置通用信息保存路径
     *
     * @param path path
     */
    void setGeneralInformationPath(String path);

    /**
     * 获取通用信息备份路径
     *
     * @return 获取通用信息备份路径
     */
    Optional<String> getGeneralInformationPathBackup();

    /**
     * 设置通用信息备份路径
     *
     * @param path path
     */
    void setGeneralInformationPathBackup(String path);

    /**
     * 获取文件保存路径
     *
     * @return 获取文件保存路径
     */
    List<String> getFileSavePaths();

    /**
     * 设置文件保存路径
     *
     * @param path path
     */
    void setFileSavePath(List<String> path);

    /**
     * 获取文件备份路径
     *
     * @return 获取文件备份路径
     */
    List<String> getFileSavePathsBackup();

    /**
     * 设置文件备份路径
     *
     * @param path pathy
     */
    void setFileSavePathBackup(List<String> path);

    /**
     * 得到剩余空间最大的文件保存路径
     *
     * @return path
     */
    String getMaxFreeSpacePath();

    /**
     * 用户数据保存路径
     *
     * @param username 用户
     * @return path
     */
    String getUserDataSavePath(String username);
}
