package com.easynas.server.dao.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.db.UserDb;
import com.easynas.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author liangyongrui
 */
@Repository
public class LoginDaoImpl implements LoginDao {

    private final UserDb userDb;

    @Autowired
    public LoginDaoImpl(UserDb userDb) {
        this.userDb = userDb;
    }

    @Override
    public User getUser(String username, String password) {
        User user = userDb.getUser(username);
        if (user.getPasswordHash().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean hasUsername(String username) {
        return userDb.getUser(username) != null;
    }

    @Override
    public User insertUser(User user) {
        return userDb.insertUser(user);
    }
}
