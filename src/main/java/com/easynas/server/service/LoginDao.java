package com.easynas.server.service;

import com.easynas.server.model.User;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author liangyongrui
 */

public interface LoginDao {
    /**
     * 根据用户名和hash密码获取用户
     *
     * @param username 用户用户名
     * @param password 密码
     * @return 完整的用户信息
     */
    Optional<User> getUser(@NonNull String username, @NonNull String password);

    /**
     * 判断用户名是否已经存在
     *
     * @param username 需要判断的用户名
     * @return 存在返回true
     */
    boolean hasUsername(@NonNull String username);

    /**
     * 插入用户
     *
     * @param user 用户信息
     * @return 失败返回null
     */
    Optional<User> insertUser(@NonNull User user);

}
