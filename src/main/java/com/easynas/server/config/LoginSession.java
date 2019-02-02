package com.easynas.server.config;

import com.easynas.server.model.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangyongrui
 */
public class LoginSession {

    /**
     * token映射User
     */
    private static final Cache<String, User> TOKEN_TO_USER = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(3, TimeUnit.DAYS)
            .build();

    public static String getNewToken(@NonNull final User user) {
        final var uuid = UUID.randomUUID().toString().replaceAll("-", "");
        TOKEN_TO_USER.put(uuid, user);
        return uuid;
    }

    public static Optional<User> getUserByToken(@NonNull final String token) {
        return Optional.ofNullable(TOKEN_TO_USER.getIfPresent(token));
    }

}
