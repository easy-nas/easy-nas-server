package com.easynas.server.ftp;

import com.easynas.server.model.User;
import com.easynas.server.service.ConfigService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

/**
 * @author liangyongrui
 */
@Slf4j
@Configuration
public class FtpConfig {

    @Value("${ftp.port}")
    private Integer ftpPort;

    private UserManager userManager;

    private final ConfigService configService;

    @Autowired
    public FtpConfig(@Qualifier("configService") @NonNull final ConfigService configService) {
        this.configService = configService;
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

    public void addUser(@NonNull final User user) {
        final var baseUser = new BaseUser();
        final var username = user.getUsername();
        baseUser.setName(username);
        baseUser.setPassword(user.getPasswordHash());
        baseUser.setHomeDirectory(configService.getUserDataSavePath(username));
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

