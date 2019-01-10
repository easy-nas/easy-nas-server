package com.easynas.server.controller;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.Result;
import com.easynas.server.model.User;
import com.easynas.server.model.request.LoginRequest;
import com.easynas.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangyongrui
 */
@RestController
@RequestMapping("api/login")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("get-login-token")
    public Result<String> getLoginToken(LoginRequest loginRequest) {

        User user = loginService.getUser(loginRequest);
        if (user != null) {
            String token = LoginSession.getNewToken(user);
            return Result.success(token);
        }
        return Result.fail("用户名或密码错误");
    }

    /**
     * 检查用户名是否存在，存在返回用户名
     */
    @PostMapping("check-username-exist")
    public Result<String> checkUsernameExist(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        if (loginService.hasUsername(username)) {
            return Result.fail("用户名已存在");
        }
        return Result.success(username);
    }

    @PostMapping("register")
    public Result<User> register(LoginRequest loginRequest) {
        User user = loginService.register(loginRequest);
        if (user != null) {
            return Result.success(user);
        }
        return Result.fail("注册失败");
    }

}
