package com.easynas.server.controller;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.Result;
import com.easynas.server.model.User;
import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.model.login.request.UsernameRequest;
import com.easynas.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
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
    public Result<String> checkUsernameExist(@RequestBody UsernameRequest usernameRequest) {
        String username = usernameRequest.getUsername();
        if (loginService.hasUsername(username)) {
            return Result.fail("用户名已存在");
        }
        return Result.success(username);
    }

    /**
     * 注册
     *
     * @param loginRequest loginRequest
     * @return 成功返回 access-token
     */
    @PostMapping("register")
    public Result<String> register(@RequestBody LoginRequest loginRequest) {
        User user = loginService.register(loginRequest);
        if (user != null) {
            String token = LoginSession.getNewToken(user);
            return Result.success(token);
        }
        return Result.fail("注册失败");
    }

}
