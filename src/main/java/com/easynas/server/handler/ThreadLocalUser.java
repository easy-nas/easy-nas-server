package com.easynas.server.handler;

import com.easynas.server.model.User;

public class ThreadLocalUser {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        threadLocal.set(user);
    }

    public static User getUser() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
