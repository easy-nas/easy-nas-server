package com.easynas.server.controller;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.Result;
import com.easynas.server.model.User;
import com.easynas.server.model.request.LoginRequest;
import com.easynas.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui
 */
@RestController
@RequestMapping("api")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("login-token")
    public Result<String> getLoginToken(LoginRequest loginRequest) {
        User user = loginService.getUser(loginRequest);
        if (user != null) {
            String token = LoginSession.getNewToken(user);
            return Result.success(token);
        }
        return Result.fail("用户名或密码错误");
    }

}
