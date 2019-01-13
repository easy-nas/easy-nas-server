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
    String setBackupGeneralInformationPath(String path);

}
