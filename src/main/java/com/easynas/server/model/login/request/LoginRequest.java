package com.easynas.server.model.login.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyongrui
 */
@Getter
@AllArgsConstructor
public class LoginRequest {
    private final String username;
    private final String password;
}
