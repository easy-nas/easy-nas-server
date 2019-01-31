package com.easynas.server.dao;

import com.easynas.server.model.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author liangyongrui
 */
@Slf4j
public abstract class BaseConfigDao extends BaseDao implements ConfigDao {

    protected File configFile;
    protected AdminConfig config;

    protected BaseConfigDao(Environment env) throws FileNotFoundException {
        String adminConfigPath = env.getProperty("adminConfigPath", "admin-config.yml");

        configFile = new File(adminConfigPath);
        log.info("config path: " + configFile.getAbsolutePath());

        if (configFile.exists()) {
            final var yaml = new Yaml();
            config = yaml.loadAs(new FileInputStream(configFile), AdminConfig.class);
        } else {
            String defaultFileSavePaths = env.getProperty("defaultFileSavePaths.master", "easy-nas-data/file/");
            String defaultFileSavePathsBackup = env.getProperty("defaultFileSavePaths.backup", "");
            String defaultGeneralInformationPath = env.getProperty("defaultGeneralInformationPath.master",
                    "easy-nas-data/general-information/");
            String defaultGeneralInformationPathBackup = env.getProperty("defaultGeneralInformationPath.backup", "");
            config = new AdminConfig();
            config.setFileSavePaths(Map.of(
                    "master", List.of(defaultFileSavePaths),
                    "backup", List.of(defaultFileSavePathsBackup)
            ));
            config.setGeneralInformationPath(Map.of(
                    "master", defaultGeneralInformationPath,
                    "backup", defaultGeneralInformationPathBackup
            ));
        }

    }
}
