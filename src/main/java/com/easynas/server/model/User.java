package com.easynas.server.model;

import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.util.HashUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author liangyongrui
 * @date 2019-01-09 22:50:37
 */
@Data
public class User {

    @Value("${password-salt}")
    private String passwordSalt;

    private String username;
    private String passwordHash;

    public User() {
    }

    public User(LoginRequest loginRequest) {
        username = loginRequest.getUsername();
        passwordHash = HashUtils.hash(loginRequest.getPassword(), passwordSalt, 32);
    }
}
