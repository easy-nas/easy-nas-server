package com.easynas.server.dao;

import com.easynas.server.ftp.FtpConfig;
import com.easynas.server.model.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户信息相关操作
 *
 * @author liangyongrui
 */
@Component
@Slf4j
public class UserDao extends BaseDao {
    private final ConfigDao configDao;
    private final FtpConfig ftpConfig;

    private Map<String, User> allUser = new HashMap<>();
    private static final String USER_INFO_YML = "user-info.yml";

    @Autowired
    public UserDao(@NonNull FtpConfig ftpConfig,
                   @Qualifier("configDao") @NonNull ConfigDao configDao) {
        this.configDao = configDao;
        this.ftpConfig = ftpConfig;
        initAllUser();
    }

    public void initAllUser() {
        final var generalInformationPath = configDao.getGeneralInformationPath();
        if (generalInformationPath.isEmpty()) {
            return;
        }
        final var root = new File(generalInformationPath);
        if (!root.exists() && !root.mkdirs()) {
            return;
        }
        allUser.clear();
        try {
            final var userDirectories = Objects.requireNonNull(root.listFiles());
            for (File userDirectory : userDirectories) {
                log.info("正在加载用户信息, " + userDirectory.getAbsolutePath());
                final var userYml = userDirectory.getAbsolutePath() + "/" + USER_INFO_YML;
                User user = new Yaml().loadAs(new FileInputStream(userYml), User.class);
                allUser.put(user.getUsername(), user);
                ftpConfig.addUser(user);
            }
        } catch (IOException e) {
            log.error("初始化用户信息失败,", e);
        }
    }

    /**
     * 插入user, 存在则更新, 系统中第一个用户为管理员
     *
     * @param user user
     * @return optional 返回user
     */
    public Optional<User> insertUser(@NonNull User user) {
        final var generalInformationPath = configDao.getGeneralInformationPath();

        String username = user.getUsername();
        String userDirectory = generalInformationPath + "/" + username;
        String userInfo = userDirectory + "/" + USER_INFO_YML;

        File file = new File(userDirectory);
        if (!file.exists() && !file.mkdirs()) {
            return Optional.empty();
        }
        if (allUser.isEmpty()) {
            user.setAdmin(true);
        }
        allUser.put(username, user);
        persist(user, userInfo);
        ftpConfig.addUser(user);
        return Optional.of(user);
    }

    public Optional<User> getUser(@NonNull String username) {
        return Optional.ofNullable(allUser.get(username));
    }

}