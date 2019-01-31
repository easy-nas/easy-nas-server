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
@Repository("configDao")
public class ConfigDaoImpl extends BaseConfigDao {

    @Autowired
    protected ConfigDaoImpl(Environment env) throws FileNotFoundException {
        super(env);
    }

    /**
     * 得到通用信息的保存路径
     */
    @Override
    public String getGeneralInformationPath() {
        return config.getGeneralInformationPath().get("master");
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    @Override
    public List<String> getFileSavePaths() {
        return config.getFileSavePaths().get("master");
    }

    @Override
    public void setGeneralInformationPath(@NonNull String path) {
        config.getGeneralInformationPath().put("master", path);
        persist(config, configFile);
    }

    @Override
    public void setFileSavePath(@NonNull List<String> paths) {
        config.getFileSavePaths().put("master", paths);
        persist(config, configFile);
    }
}
