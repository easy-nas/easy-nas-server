package com.easynas.server.service.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.model.User;
import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.service.LoginService;
import com.easynas.server.util.HashUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author liangyongrui
 */
@Service
public class LoginServiceImpl implements LoginService {


    private final LoginDao loginDao;

    @Autowired
    public LoginServiceImpl(@NonNull LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public Optional<User> getUser(@NonNull LoginRequest loginRequest) {
        User user = getUserByLoginRequest(loginRequest);
        return loginDao.getUser(user.getUsername(), user.getPasswordHash());
    }

    @Override
    public Optional<User> register(@NonNull LoginRequest loginRequest) {
        User user = getUserByLoginRequest(loginRequest);
        if (!loginDao.hasUsername(user.getUsername()) && loginDao.insertUser(user).isPresent()) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public boolean hasUsername(@NonNull String username) {
        return loginDao.hasUsername(username);
    }


    private User getUserByLoginRequest(@NonNull LoginRequest loginRequest) {
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPasswordHash(HashUtils.hash(loginRequest.getPassword()));
        return user;
    }
}
