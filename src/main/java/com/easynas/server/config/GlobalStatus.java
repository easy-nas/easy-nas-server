package com.easynas.server.config;

/**
 * 保存一些全局状态
 *
 * @author liangyongrui
 */
public class GlobalStatus {

    /**
     * 是否上锁，上锁后所有操作的不能执行
     */
    private static boolean lock = false;

    public static boolean isLock() {
        return lock;
    }

    public static void setLock(boolean lock) {
        GlobalStatus.lock = lock;
    }

}
