package com.easynas.server.db;

import com.easynas.server.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户信息相关操作
 *
 * @author liangyongrui
 */
@Component
@Slf4j
public class UserDb extends BaseDb {
    private final ConfigDb configDb;

    private Map<String, User> allUser = new HashMap<>();
    private static final String USER_INFO_YML = "user-info.yml";

    @Autowired
    public UserDb(ConfigDb configDb) throws FileNotFoundException {
        this.configDb = configDb;
        String generalInformationPath = configDb.getGeneralInformationPath();
        File root = new File(generalInformationPath);
        if (!root.exists() && !root.mkdirs()) {
            return;
        }
        for (File userDirectory : Objects.requireNonNull(root.listFiles())) {
            log.info("正在加载用户信息, " + userDirectory.getAbsolutePath());
            String userYml = userDirectory.getAbsolutePath() + "/" + USER_INFO_YML;
            Yaml yaml = new Yaml();
            User user = yaml.loadAs(new FileInputStream(userYml), User.class);
            allUser.put(user.getUsername(), user);
        }
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
        String userDirectory = generalInformationPath + "/" + username;
        String userInfo = userDirectory + "/" + USER_INFO_YML;

        File file = new File(userDirectory);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        allUser.put(username, user);
        persist(user, userInfo);
        return user;
    }

    public User getUser(String username) {
        return allUser.get(username);
    }
}
