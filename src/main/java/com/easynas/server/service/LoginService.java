package com.easynas.server.service;

import com.easynas.server.model.User;
import com.easynas.server.model.login.request.LoginRequest;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author liangyongrui
 */
public interface LoginService {

    /**
     * 根据登录信息得到user
     *
     * @param loginRequest 登录信息
     * @return 返回user，如果用户名密码不匹配返回空
     */
    Optional<User> getUser(@NonNull final LoginRequest loginRequest);

    /**
     * 注册
     *
     * @param loginRequest 注册信息
     * @return 成功返回user，注册失败返回空
     */
    Optional<User> register(@NonNull final LoginRequest loginRequest);

    /**
     * 判断用户名是否已经存在
     *
     * @param username 需要判断的用户名
     * @return 存在返回true
     */
    boolean hasUsername(@NonNull final String username);
}
