package com.easynas.server.dao;

import com.easynas.server.model.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午6:11
 */
@Slf4j
public abstract class BaseConfigDao extends BaseDao implements ConfigDao {

    protected File filePath;
    protected AdminConfig config;

    protected BaseConfigDao() throws FileNotFoundException {
        filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "admin-config.yml");
        log.info("config path: " + filePath.getAbsolutePath());
        final var yaml = new Yaml();
        config = yaml.loadAs(new FileInputStream(filePath), AdminConfig.class);
    }
}
