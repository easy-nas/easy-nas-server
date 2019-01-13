package com.easynas.server.db;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public abstract class BaseDb {
    /**
     * 持久化配置
     */
    protected <T> void persist(T yamlObject, String filePath) {
        Yaml yaml = new Yaml();
        try {
            yaml.dump(yamlObject, new FileWriter(filePath));
        } catch (IOException e) {
            log.error("持久化配置失败", e);
        }
    }
}
