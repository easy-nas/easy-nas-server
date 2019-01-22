package com.easynas.server.dao;

import lombok.NonNull;

import java.util.List;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
public interface ConfigDao {

    /**
     * 得到通用信息的保存路径
     *
     * @return path
     */
    String getGeneralInformationPath();

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    List<String> getFileSavePaths();

    /**
     * 设置通用信息保存路径
     *
     * @param path path
     */
    void setGeneralInformationPath(@NonNull String path);

    /**
     * 设置文件保存路径
     *
     * @param paths paths
     */
    void setFileSavePath(@NonNull List<String> paths);

}
