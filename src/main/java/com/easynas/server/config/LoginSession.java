package com.easynas.server.config;

import com.easynas.server.model.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LoginSession {

    /**
     * token映射User
     */
    private static Cache<String, User> tokenToUser = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(3, TimeUnit.DAYS)
            .build();

    public static String getNewToken(User user) {
        String uuid = UUID.randomUUID().toString();
        tokenToUser.put(uuid, user);
        return uuid;
    }

    public static User getUserByToken(String token) {
        return tokenToUser.getIfPresent(token);
    }

}
