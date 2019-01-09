package com.easynas.server.dao;

import com.easynas.server.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author liangyongrui
 */
@Repository
public interface LoginDao {
    /**
     * 根据用户名和hash密码获取用户
     *
     * @param user 用户信息
     * @return 完整的用户信息
     */
    User getUser(User user);
}
