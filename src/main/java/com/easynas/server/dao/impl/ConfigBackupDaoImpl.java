package com.easynas.server.dao.impl;

import com.easynas.server.dao.BaseConfigDao;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
@Slf4j
@Repository("configBackupDao")
public class ConfigBackupDaoImpl extends BaseConfigDao {

    @Autowired
    protected ConfigBackupDaoImpl(@NonNull final Environment env) throws FileNotFoundException {
        super(env);
    }

    /**
     * 得到通用信息的保存路径
     */
    @Override
    public String getGeneralInformationPath() {
        return config.getGeneralInformationPath().get("backup");
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    @Override
    public List<String> getFileSavePaths() {
        return config.getFileSavePaths().get("backup");
    }

    @Override
    public void setGeneralInformationPath(@NonNull final String path) {
        config.getGeneralInformationPath().put("backup", path);
        persist(config, configFile);
    }

    @Override
    public void setFileSavePath(@NonNull final List<String> paths) {
        config.getFileSavePaths().put("backup", paths);
        persist(config, configFile);
    }
}
