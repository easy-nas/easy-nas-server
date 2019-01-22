package com.easynas.server.db;

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
public abstract class BaseDb {
    /**
     * 持久化配置
     */
    <T> void persist(@NonNull T yamlObject,@NonNull String filePath) {
        persist(yamlObject, new File(filePath));
    }

    <T> void persist(@NonNull T yamlObject,@NonNull File file) {
        final var yaml = new Yaml();
        try {
            yaml.dump(yamlObject, new FileWriter(file));
        } catch (IOException e) {
            log.error("持久化配置失败", e);
        }
    }
}
