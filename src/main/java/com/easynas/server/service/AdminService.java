package com.easynas.server.service;

import lombok.NonNull;

import java.util.Optional;

/**
 * @author liangyongrui
 */
public interface AdminService {
    /**
     * 设置通用信息保存路径
     *
     * @param path path
     * @return 错误信息, 成功无错误信息
     */
    Optional<String> setGeneralInformationPath(@NonNull final String path);

    /**
     * 增加文件存储路径， 路径必须与现有的路径不在一个磁盘中
     *
     * @param path 需要新增的路径
     * @return 错误信息, 成功无错误信息
     */
    Optional<String> addFileSavePath(@NonNull final String path);

    /**
     * 删除文件保存路径
     *
     * @param path 指定的路径
     * @return 错误信息, 成功无错误信息
     */
    Optional<String> deleteFileSavePath(@NonNull final String path);

}
