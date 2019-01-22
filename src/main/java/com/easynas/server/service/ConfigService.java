package com.easynas.server.service;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liangyongrui
 */
public interface ConfigService {

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
    void setGeneralInformationPath(@NonNull String path);

    /**
     * 用户数据保存路径
     *
     * @param username 用户
     * @return path
     */
    String getUserDataSavePath(@NonNull String username);
}
