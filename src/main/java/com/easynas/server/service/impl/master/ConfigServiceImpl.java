package com.easynas.server.service.impl.master;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.ConfigService;
import com.easynas.server.service.base.BaseConfigService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午4:46
 */
@Slf4j
@Service("configService")
public class ConfigServiceImpl extends BaseConfigService implements ConfigService {

    @Autowired
    protected ConfigServiceImpl(@Qualifier("configDao") @NonNull ConfigDao configDao) {
        super(configDao);
    }

    @Override
    public String getGeneralInformationPath() {
        return configDao.getGeneralInformationPath();
    }

    @Override
    public void setGeneralInformationPath(@NonNull String s) {
        configDao.setGeneralInformationPath(s);
    }

    /**
     * 用户数据保存路径
     */
    @Override
    public String getUserDataSavePath(@NonNull String username) {
        return configDao.getGeneralInformationPath() + "/" + username + "/data";
    }
}
