package com.easynas.server.service.impl;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.ConfigService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author liangyongrui
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    @Bean("configService")
    public ConfigService getConfigService(@Qualifier("configDao") @NonNull final ConfigDao configDao) {
        return new ConfigServiceImpl(configDao);
    }

    @Autowired
    @Bean("configBackupService")
    public ConfigService getConfigBackupService(@Qualifier("configDao") @NonNull final ConfigDao configDao) {
        return new ConfigServiceImpl(configDao);
    }

    private final ConfigDao configDao;

    protected ConfigServiceImpl(@NonNull final ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public String getGeneralInformationPath() {
        return configDao.getGeneralInformationPath();
    }

    @Override
    public void setGeneralInformationPath(@NonNull final String s) {
        configDao.setGeneralInformationPath(s);
    }

    /**
     * 用户数据保存路径
     */
    @Override
    public String getUserDataSavePath(@NonNull final String username) {
        return configDao.getGeneralInformationPath() + "/" + username + "/data";
    }
}
