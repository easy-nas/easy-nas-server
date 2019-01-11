package com.easynas.server.service.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.model.User;
import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liangyongrui
 */
@Service
public class LoginServiceImpl implements LoginService {


    private final LoginDao loginDao;

    @Autowired
    public LoginServiceImpl(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public User getUser(LoginRequest loginRequest) {
        User user = new User(loginRequest);
        return loginDao.getUser(user);
    }

    @Override
    public User register(LoginRequest loginRequest) {
        User user = new User(loginRequest);
        if (loginDao.insertUser(user) > 0) {
            return user;
        }
        return null;
    }

    @Override
    public boolean hasUsername(String username) {
        return loginDao.hasUsername(username);
    }

}
