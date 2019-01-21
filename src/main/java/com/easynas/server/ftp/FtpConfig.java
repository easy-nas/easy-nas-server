package com.easynas.server.ftp;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.db.UserDb;
import lombok.NonNull;
import org.apache.ftpserver.ftplet.FtpException;
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
@Configuration
public class FtpConfig {

    @Value("${ftp.port}")
    private Integer ftpPort;

    private final UserDb userDb;
    private final ConfigDao configDao;

    @Autowired
    public FtpConfig(@NonNull UserDb userDb, @NonNull ConfigDao configDao) {
        this.userDb = userDb;
        this.configDao = configDao;
    }

    @PostConstruct
    public void init() throws FtpException {
        final var ftpServerContext = new FtpServerContext();
        ftpServerContext.addListener("default", getLister());
        ftpServerContext.setFtplets(Map.of("default", getEasyNasFtplet()));
        final var userManager = ftpServerContext.getUserManager();
        for (final var u : userDb.getAllUser()) {
            final var user = new BaseUser();
            final var username = u.getUsername();
            user.setName(username);
            user.setPassword(u.getPasswordHash());
            user.setHomeDirectory(configDao.getUserDataSavePath(username));
            user.setAuthorities(List.of(new WritePermission()));
            userManager.save(user);
        }
        final var server = ftpServerContext.getFtpServer();
        server.start();
    }

    private Listener getLister() {
        final var factory = new ListenerFactory();
        factory.setPort(ftpPort);
        return factory.createListener();
    }

    @Bean
    public EasyNasFtplet getEasyNasFtplet() {
        return new EasyNasFtplet();
    }
}

