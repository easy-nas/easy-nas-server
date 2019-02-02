package com.easynas.server.dao;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author liangyongrui
 */
@Slf4j
public abstract class BaseDao {
    /**
     * 持久化配置
     */
    <T> void persist(@NonNull final T yamlObject, @NonNull final String filePath) {
        persist(yamlObject, new File(filePath));
    }

    protected <T> void persist(@NonNull final T yamlObject, @NonNull final File file) {
        try {
            new Yaml().dump(yamlObject, new FileWriter(file));
        } catch (IOException e) {
            log.error("持久化配置失败", e);
        }
    }
}
