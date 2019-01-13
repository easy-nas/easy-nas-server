package com.easynas.server.model;

import lombok.Data;

/**
 * @author liangyongrui
 * @date 2019-01-09 22:50:37
 */
@Data
public class User {

    private String username;
    private String passwordHash;
    /**
     * 是否为管理员
     */
    private boolean admin;

    public User() {
    }
}
