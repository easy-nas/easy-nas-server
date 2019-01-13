package com.easynas.server.handler;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.User;
import com.easynas.server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author liangyongrui
 * @date 19-1-9 下午8:55
 */
@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final String LOGIN_ERROR_JSON = JsonUtils.toJsonString(Map.of(
            "code", "login-error", "message", "登录验证失败请重新登录"));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String header = request.getHeader("access-token");
        if (header == null) {
            sendLoginError(response);
            return false;
        }
        User user = LoginSession.getUserByToken(header);
        if (user == null) {
            sendLoginError(response);
            return false;
        }
        ThreadLocalUser.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
        ThreadLocalUser.remove();
    }

    private void sendLoginError(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(LOGIN_ERROR_JSON);
    }
}
