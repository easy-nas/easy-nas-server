package com.easynas.server.handler;

import com.easynas.server.model.User;

/**
 * 获取当前线程登录的用户
 *
 * @author liangyongrui
 */
public class ThreadLocalUser {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        threadLocal.set(user);
    }

    public static User getUser() {
        return threadLocal.get();
    }

    static void remove() {
        threadLocal.remove();
    }
}
