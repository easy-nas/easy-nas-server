package com.easynas.server.db;

import com.easynas.server.model.AdminConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 修改读取config配置
 *
 * @author liangyongrui
 */
@Slf4j
@Component
public class ConfigDb extends BaseDb {

    private File filePath;
    private AdminConfig config;

    @PostConstruct
    private void init() throws FileNotFoundException {
        filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "admin-config.yml");
        log.info("config path: " + filePath.getAbsolutePath());
        final var yaml = new Yaml();
        config = yaml.loadAs(new FileInputStream(filePath), AdminConfig.class);
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
    public Optional<String> getGeneralInformationPathBackup() {
        return Optional.ofNullable(config.getGeneralInformationPath().get("backup"));
    }

    /**
     * 得到真实文件的保存路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getFileSavePaths() {
        return config.getFileSavePaths().get("master");
    }

    /**
     * 得到真实文件的备份路径
     *
     * @return 路径有多个，所以返回list
     */
    public List<String> getFileSavePathsBackup() {
        return config.getFileSavePaths().computeIfAbsent("backup", t -> new ArrayList<>());
    }

    public void setGeneralInformationPath(@NonNull String path) {
        config.getGeneralInformationPath().put("master", path);
        persist(config, filePath);
    }

    public void setGeneralInformationPathBackup(@NonNull String path) {
        config.getGeneralInformationPath().put("backup", path);
        persist(config, filePath);
    }

    public void setFileSavePath(@NonNull List<String> paths) {
        config.getFileSavePaths().put("master", paths);
        persist(config, filePath);
    }

    public void setFileSavePathBackup(@NonNull List<String> paths) {
        config.getFileSavePaths().put("backup", paths);
        persist(config, filePath);
    }

    @Bean
    public ConfigDb getInstance() {
        return new ConfigDb();
    }
}
