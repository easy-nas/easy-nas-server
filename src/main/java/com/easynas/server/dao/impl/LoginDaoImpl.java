package com.easynas.server.dao.impl;

import com.easynas.server.dao.LoginDao;
import com.easynas.server.db.ConfigDb;
import com.easynas.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDaoImpl implements LoginDao {

    @Autowired
    ConfigDb easyNasConfig;

    @Override
    public User getUser(User user) {
        return null;
    }

    @Override
    public boolean hasUsername(String username) {
        return false;
    }

    @Override
    public int insertUser(User user) {
        return 0;
    }
}
