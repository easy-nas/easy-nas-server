package com.easynas.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyongrui
 */
@AllArgsConstructor
@Getter
public class User {

    private final String username;
    private final String passwordHash;
    /**
     * 是否为管理员
     */
    private final Boolean admin;
}
