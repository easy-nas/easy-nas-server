package com.easynas.server.dao.impl;

import com.easynas.server.dao.BaseConfigDao;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
@Slf4j
@Repository("configBackupDao")
public class ConfigBackupDaoImpl extends BaseConfigDao {

    @Override
    @PostConstruct
    protected void init() throws FileNotFoundException {
        super.init();
    }

    /**
     * 得到通用信息的保存路径
     */
    public String getGeneralInformationPath() {
        return config.getGeneralInformationPath().get("backup");
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getFileSavePaths() {
        return config.getFileSavePaths().get("backup");
    }

    public void setGeneralInformationPath(@NonNull String path) {
        config.getGeneralInformationPath().put("backup", path);
        persist(config, filePath);
    }

    public void setFileSavePath(@NonNull List<String> paths) {
        config.getFileSavePaths().put("backup", paths);
        persist(config, filePath);
    }
}
