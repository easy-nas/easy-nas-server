package com.easynas.server.db;

import com.easynas.server.model.ConfigModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
@Slf4j
@Component
public class ConfigDb {

    @Value("${workConfigPath}")
    private String workConfigPath;

    private String filePath;
    private ConfigModel config;

    @PostConstruct
    private void init() throws FileNotFoundException {
        File path = new File("");
        filePath = path.getAbsolutePath() + "/" + workConfigPath + "/config.yml";
        log.info("config path: " + filePath);
        Yaml yaml = new Yaml();
        config = yaml.loadAs(new FileInputStream(filePath), ConfigModel.class);
    }

    /**
     * 得到通用信息的保存路径
     */
    public String getGeneralInformationPath() {
        return config.getGeneralInformationPath().get("master");
    }

    /**
     * 得到通用信息的备份路径
     */
    public String getBackupGeneralInformationPath() {
        return config.getGeneralInformationPath().get("backup");
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getFileSavePath() {
        return config.getFileSavePath().get("master");
    }

    /**
     * 得到真实文件的备份路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getBackupFileSavePath() {
        return config.getFileSavePath().get("backup");
    }


    public void setGeneralInformationPath(String path) {
        config.getGeneralInformationPath().put("master", path);
        persistConfig();
    }

    public void setBackupGeneralInformationPath(String path) {
        config.getGeneralInformationPath().put("backup", path);
        persistConfig();
    }

    public void setFileSavePath(List<String> paths) {
        config.getFileSavePath().put("master", paths);
        persistConfig();
    }

    public void setBackupFileSavePath(List<String> paths) {
        config.getFileSavePath().put("backup", paths);
        persistConfig();
    }

    /**
     * 持久化配置
     */
    private void persistConfig() {
        Yaml yaml = new Yaml();
        try {
            yaml.dump(config, new FileWriter(filePath));
        } catch (IOException e) {
            log.error("持久化配置失败", e);
        }
    }

    @Bean
    public ConfigDb getInstance() {
        return new ConfigDb();
    }
}
