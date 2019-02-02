package com.easynas.server.controller;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.Result;
import com.easynas.server.model.login.request.LoginRequest;
import com.easynas.server.model.login.request.UsernameRequest;
import com.easynas.server.service.LoginService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
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
    public LoginController(@NonNull final LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "获取登录的access-token",
            notes = "header中不需要有access-token即可获取, 获取后所有的请求都在header中带上这个access-token")
    @PostMapping("login")
    public Result<String> login(@RequestBody @NonNull final LoginRequest loginRequest) {
        return loginService.getUser(loginRequest).map(LoginSession::getNewToken).map(Result::success)
                .orElseGet(() -> Result.fail("用户名或密码错误"));
    }

    /**
     * 检查用户名是否存在，存在返回用户名
     */
    @ApiOperation(value = "检查用户名是否存在", notes = "header中不需要有access-token即可获取")
    @PostMapping("check-username-exist")
    public Result<String> checkUsernameExist(@RequestBody @NonNull final UsernameRequest usernameRequest) {
        final var username = usernameRequest.getUsername();
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
    @ApiOperation(value = "注册",
            notes = "header中不需要有access-token即可获取, 获取后所有的请求都在header中带上这个access-token")
    @PostMapping("register")
    public Result<String> register(@RequestBody @NonNull final LoginRequest loginRequest) {
        return loginService.register(loginRequest).map(LoginSession::getNewToken).map(Result::success)
                .orElseGet(() -> Result.fail("注册失败"));
    }

}
