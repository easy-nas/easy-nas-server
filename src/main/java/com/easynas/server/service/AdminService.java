package com.easynas.server.service;

/**
 * @author liangyongrui
 */
public interface AdminService {
    /**
     * 设置通用信息保存路径
     *
     * @param path path
     * @return 错误信息，成功返回null
     */
    String setGeneralInformationPath(String path);


    /**
     * 设置通用信息备份路径
     *
     * @param path path
     * @return 错误信息，成功返回null
     */
    String setGeneralInformationPathBackup(String path);


    /**
     * 增加文件存储路径， 路径必须与现有的路径不在一个磁盘中
     *
     * @param path 需要新增的路径
     * @return 错误信息，成功返回null
     */
    String addFileSavePath(String path);

    /**
     * 增加文件备份路径， 路径必须与现有的路径不在一个磁盘中
     *
     * @param path 需要新增的路径
     * @return 错误信息，成功返回null
     */
    String addFileSavePathBackup(String path);
}
