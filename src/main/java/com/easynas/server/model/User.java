package com.easynas.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyongrui
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private String username;
    private String passwordHash;
    /**
     * 是否为管理员
     */
    private Boolean admin;
}
