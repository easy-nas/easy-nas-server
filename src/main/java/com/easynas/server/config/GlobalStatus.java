package com.easynas.server.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo 思考并发状态可能出现的问题
 * 保存一些全局状态
 *
 * @author liangyongrui
 */
public class GlobalStatus {

    /**
     * 是否上锁，上锁后所有操作的不能执行
     * 有用户正在上传文件的时候不能上锁
     */
    private static AtomicBoolean lock = new AtomicBoolean(false);

    /**
     * 用户正在上传的数量
     */
    private static AtomicInteger uploadCount = new AtomicInteger(0);

    public static boolean isLock() {
        return lock.get();
    }

    /**
     * 只有当没有用户上传文件的时候才能加锁
     *
     * @param lock lock status
     * @return 加锁是否成功
     */
    public static boolean setLock(boolean lock) {
        if (getUploadCount() != 0) {
            return false;
        }
        GlobalStatus.lock.set(lock);
        return true;
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
