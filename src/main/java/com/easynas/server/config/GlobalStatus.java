package com.easynas.server.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 保存一些全局状态
 *
 * @author liangyongrui
 */
public class GlobalStatus {

    /**
     * 是否上锁，上锁后所有操作的不能执行
     */
    private static AtomicBoolean lock = new AtomicBoolean(false);

    /**
     * 用户正在上传的数量
     */
    private static AtomicInteger uploadCount = new AtomicInteger(0);

    public static boolean isLock() {
        return lock.get();
    }

    public static void setLock(boolean lock) {
        GlobalStatus.lock.set(lock);
    }


    /**
     * 用户上传数量加1
     */
    private static void uploadCountIncrement() {
        uploadCount.getAndIncrement();
    }

    /**
     * 得到用户正在上传的数量
     */
    private static int getUploadCount() {
        return uploadCount.get();
    }
}
