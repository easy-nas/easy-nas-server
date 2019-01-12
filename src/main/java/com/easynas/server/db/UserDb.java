package com.easynas.server.db;

import com.easynas.server.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息相关操作
 *
 * @author liangyongrui
 */
@Component
@Slf4j
public class UserDb {
    private final ConfigDb configDb;

    private Map<String, User> allUser = new HashMap<>();

    @Autowired
    public UserDb(ConfigDb configDb) {
        this.configDb = configDb;
    }

    /**
     * 插入user, 存在则更新
     *
     * @param user user
     * @return 插入失败返回null, 成功返回user
     */
    public User insertUser(User user) {
        String generalInformationPath = configDb.getGeneralInformationPath();
        String username = user.getUsername();
        File file = new File(generalInformationPath + "/" + username);
        if (!file.exists()) {
            if (file.mkdirs()) {
                allUser.put(username, user);
            } else {
                return null;
            }
        }
        persist(user, "");
        return user;
    }

    /**
     * 持久化配置
     */
    private void persist(User user, String filePath) {
        Yaml yaml = new Yaml();
        try {
            yaml.dump(user, new FileWriter(filePath));
        } catch (IOException e) {
            log.error("持久化配置失败", e);
        }
    }

    public User getUser(String username) {
        return allUser.get(username);
    }
}
