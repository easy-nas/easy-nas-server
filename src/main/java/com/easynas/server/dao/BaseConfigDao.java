package com.easynas.server.dao;

import com.easynas.server.model.AdminConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liangyongrui
 */
@Slf4j
public abstract class BaseConfigDao extends BaseDao implements ConfigDao {

    protected final File configFile;
    protected final AdminConfig config;

    protected BaseConfigDao(@NonNull final Environment env) {
        final var adminConfigPath = env.getProperty("adminConfigPath", "admin-config.yml");
        configFile = new File(adminConfigPath);
        log.info("config path: " + configFile.getAbsolutePath());
        config = Optional.of(configFile)
                .filter(File::exists)
                .map(t -> {
                    try {
                        return new Yaml().loadAs(new FileInputStream(t), AdminConfig.class);
                    } catch (FileNotFoundException e) {
                        log.error("载入配置文件失败，", e);
                        return null;
                    }
                }).orElseGet(AdminConfig::new);
        if (config.getFileSavePaths() == null) {
            final var defaultFileSavePaths = env.getProperty("defaultFileSavePaths.master", "easy-nas-data/file/");
            final var defaultFileSavePathsBackup = env.getProperty("defaultFileSavePaths.backup", "");
            config.getFileSavePaths().putAll(Map.of(
                    "master", List.of(defaultFileSavePaths),
                    "backup", List.of(defaultFileSavePathsBackup)
            ));
        }
        if (config.getGeneralInformationPath() == null) {
            String defaultGeneralInformationPath = env.getProperty("defaultGeneralInformationPath.master",
                    "easy-nas-data/general-information/");
            String defaultGeneralInformationPathBackup = env.getProperty("defaultGeneralInformationPath.backup", "");
            config.setGeneralInformationPath(Map.of(
                    "master", defaultGeneralInformationPath,
                    "backup", defaultGeneralInformationPathBackup
            ));
        }
    }
}
