package com.easynas.server.dao.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.db.UserDb;
import com.easynas.server.model.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author liangyongrui
 */
@Repository
public class LoginDaoImpl implements LoginDao {

    private final UserDb userDb;

    @Autowired
    public LoginDaoImpl(@NonNull UserDb userDb) {
        this.userDb = userDb;
    }

    @Override
    public Optional<User> getUser(@NonNull String username,@NonNull String password) {
        return userDb.getUser(username).filter(t -> t.getPasswordHash().equals(password));
    }

    @Override
    public boolean hasUsername(@NonNull String username) {
        return userDb.getUser(username).isPresent();
    }

    @Override
    public Optional<User> insertUser(@NonNull  User user) {
        return userDb.insertUser(user);
    }
}
