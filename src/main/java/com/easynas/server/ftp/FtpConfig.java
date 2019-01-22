package com.easynas.server.ftp;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.db.UserDb;
import com.easynas.server.model.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-21 下午7:58
 */
@Slf4j
@Configuration
public class FtpConfig {

    @Value("${ftp.port}")
    private Integer ftpPort;

    private UserManager userManager;

    private final ConfigDao configDao;

    @Autowired
    public FtpConfig(@NonNull ConfigDao configDao) {
        this.configDao = configDao;
    }

    @PostConstruct
    public void init() throws FtpException {
        final var ftpServerContext = new FtpServerContext();
        ftpServerContext.addListener("default", getLister());
        ftpServerContext.setFtplets(Map.of("default", getEasyNasFtplet()));
        userManager = ftpServerContext.getUserManager();

        final var server = ftpServerContext.getFtpServer();
        server.start();
    }

    private Listener getLister() {
        final var factory = new ListenerFactory();
        factory.setPort(ftpPort);
        return factory.createListener();
    }

    public void addUser(User user) {
        final var baseUser = new BaseUser();
        final var username = user.getUsername();
        baseUser.setName(username);
        baseUser.setPassword(user.getPasswordHash());
        baseUser.setHomeDirectory(configDao.getUserDataSavePath(username));
        baseUser.setAuthorities(List.of(new WritePermission()));
        try {
            userManager.save(baseUser);
        } catch (FtpException e) {
            log.error("ftp服务添加用户失败: " + username, e);
        }
    }

    @Bean
    public EasyNasFtplet getEasyNasFtplet() {
        return new EasyNasFtplet();
    }
}

