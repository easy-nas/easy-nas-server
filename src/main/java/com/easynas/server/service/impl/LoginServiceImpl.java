package com.easynas.server.service.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.model.User;
import com.easynas.server.model.request.LoginRequest;
import com.easynas.server.service.LoginService;
import com.easynas.server.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author liangyongrui
 */
public class LoginServiceImpl implements LoginService {

    @Value("${password-salt}")
    private String passwordSalt;

    private final LoginDao loginDao;

    @Autowired
    public LoginServiceImpl(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public User getUser(LoginRequest loginRequest) {
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPasswordHash(HashUtils.hash(loginRequest.getPassword(), passwordSalt, 32));
        return loginDao.getUser(user);
    }

}
