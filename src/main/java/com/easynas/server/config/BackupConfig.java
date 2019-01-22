package com.easynas.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 备份线程
 *
 * @author liangyongrui
 */
@Slf4j
@Configuration
public class BackupConfig {

    /**
     * 检查时间间隔（单位min)
     */
    private static final int CHECK_TIME_INTERVAL = 30;



}
