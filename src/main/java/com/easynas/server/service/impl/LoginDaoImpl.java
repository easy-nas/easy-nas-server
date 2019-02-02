package com.easynas.server.service.impl;

import com.easynas.server.dao.UserDao;
import com.easynas.server.model.User;
import com.easynas.server.service.LoginDao;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author liangyongrui
 */
@Repository
public class LoginDaoImpl implements LoginDao {

    private final UserDao userDb;

    @Autowired
    public LoginDaoImpl(@NonNull final UserDao userDb) {
        this.userDb = userDb;
    }

    @Override
    public Optional<User> getUser(@NonNull final String username, @NonNull final String password) {
        return userDb.getUser(username).filter(t -> t.getPasswordHash().equals(password));
    }

    @Override
    public boolean hasUsername(@NonNull final String username) {
        return userDb.getUser(username).isPresent();
    }

    @Override
    public Optional<User> insertUser(@NonNull final User user) {
        return userDb.insertUser(user);
    }
}
