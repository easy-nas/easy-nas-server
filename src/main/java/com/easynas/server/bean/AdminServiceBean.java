package com.easynas.server.bean;

import com.easynas.server.dao.UserDao;
import com.easynas.server.handler.RuntimeHandler;
import com.easynas.server.service.AdminService;
import com.easynas.server.service.ConfigService;
import com.easynas.server.service.FileService;
import com.easynas.server.service.impl.AdminServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author liangyongrui
 */
@Component
public class AdminServiceBean {

    @Bean("adminService")
    @Autowired
    public AdminService getAdminService(
            @NonNull final UserDao userDao,
            @NonNull final RuntimeHandler runtimeHandler,
            @Qualifier("configService") @NonNull final ConfigService configService,
            @Qualifier("fileService") @NonNull final FileService fileService) {
        return new AdminServiceImpl(userDao, runtimeHandler, configService, fileService, "master");
    }

    @Bean("adminBackupService")
    @Autowired
    public AdminService getAdminBackupService(
            @NonNull final UserDao userDao,
            @NonNull final RuntimeHandler runtimeHandler,
            @Qualifier("configBackupService") @NonNull final ConfigService configService,
            @Qualifier("fileBackupService") @NonNull final FileService fileService) {
        return new AdminServiceImpl(userDao, runtimeHandler, configService, fileService, "backup");
    }
}
