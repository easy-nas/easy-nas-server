package com.easynas.server.service.base;

import com.easynas.server.dao.ConfigDao;
import com.easynas.server.service.ConfigService;
import lombok.NonNull;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午4:45
 */
public abstract class BaseConfigService implements ConfigService {

    protected ConfigDao configDao;

    protected BaseConfigService(@NonNull ConfigDao configDao) {
        this.configDao = configDao;
    }

}
