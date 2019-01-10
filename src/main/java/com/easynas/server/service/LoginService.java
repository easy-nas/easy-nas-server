package com.easynas.server.service;

import com.easynas.server.model.User;
import com.easynas.server.model.request.LoginRequest;

/**
 * @author liangyongrui
 */

public interface LoginService {

    User getUser(LoginRequest loginRequest);

    User register(LoginRequest loginRequest);

    /**
     * 判断用户名是否已经存在
     *
     * @param username 需要判断的用户名
     * @return 存在返回true
     */
    boolean hasUsername(String username);
}
