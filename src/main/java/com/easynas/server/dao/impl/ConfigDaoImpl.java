package com.easynas.server.dao.impl;

import com.easynas.server.dao.BaseConfigDao;
import com.easynas.server.dao.BaseDao;
import com.easynas.server.model.AdminConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
@Slf4j
@Repository("configDao")
public class ConfigDaoImpl extends BaseConfigDao {

    @Override
    @PostConstruct
    protected void init() throws FileNotFoundException {
        super.init();
    }

    /**
     * 得到通用信息的保存路径
     */
    public String getGeneralInformationPath() {
        return config.getGeneralInformationPath().get("master");
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getFileSavePaths() {
        return config.getFileSavePaths().get("master");
    }

    public void setGeneralInformationPath(@NonNull String path) {
        config.getGeneralInformationPath().put("master", path);
        persist(config, filePath);
    }

    public void setFileSavePath(@NonNull List<String> paths) {
        config.getFileSavePaths().put("master", paths);
        persist(config, filePath);
    }
}
