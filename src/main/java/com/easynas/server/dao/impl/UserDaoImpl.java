package com.easynas.server.dao.impl;

import com.easynas.server.dao.UserDao;
import com.easynas.server.db.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-22 下午2:49
 */
@Repository
public class UserDaoImpl implements UserDao {

    private final UserDb userDb;

    @Autowired
    public UserDaoImpl(UserDb userDb) {
        this.userDb = userDb;
    }

    @Override
    public void initAllUser() {
        userDb.initAllUser();
    }
}
