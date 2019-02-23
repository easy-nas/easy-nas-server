package com.easynas.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.easynas.server.constant.CommonConstant.PRODUCTION_SPRING_PROFILES_ACTIVE;

/**
 * @author liangyongrui
 */
@Component
@Slf4j
public class RuntimeHandler {
    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    public void runInProduction(Runnable runnable) {
        if (PRODUCTION_SPRING_PROFILES_ACTIVE.equals(springProfilesActive)) {
            runnable.run();
        }
    }

    public <T> T supplyInProduction(Supplier<T> supplier, T defaultReturn) {
        if (PRODUCTION_SPRING_PROFILES_ACTIVE.equals(springProfilesActive)) {
            defaultReturn = supplier.get();
        }
        return defaultReturn;
    }

}
