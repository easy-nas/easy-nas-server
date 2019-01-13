package com.easynas.server.service.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.model.User;
import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.service.LoginService;
import com.easynas.server.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author liangyongrui
 */
@Service
public class LoginServiceImpl implements LoginService {


    private final LoginDao loginDao;
    @Value("${password-salt}")
    private String passwordSalt;

    @Autowired
    public LoginServiceImpl(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public User getUser(LoginRequest loginRequest) {
        User user = getUserByLoginRequest(loginRequest);
        return loginDao.getUser(user.getUsername(), user.getPasswordHash());
    }

    @Override
    public User register(LoginRequest loginRequest) {
        User user = getUserByLoginRequest(loginRequest);
        if (!loginDao.hasUsername(user.getUsername()) && loginDao.insertUser(user) != null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean hasUsername(String username) {
        return loginDao.hasUsername(username);
    }


    private User getUserByLoginRequest(LoginRequest loginRequest) {
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPasswordHash(HashUtils.hash(loginRequest.getPassword(), passwordSalt, 32));
        return user;
    }
}
