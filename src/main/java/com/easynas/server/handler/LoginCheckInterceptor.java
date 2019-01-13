package com.easynas.server.handler;

import com.easynas.server.config.LoginSession;
import com.easynas.server.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.easynas.server.util.JsonUtils.toJsonString;

/**
 * @author liangyongrui
 * @date 19-1-9 下午8:55
 */
@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final String LOGIN_ERROR_JSON = toJsonString(Map.of(
            "code", "login_error", "message", "登录验证失败，请重新登录"));


    private static final String PERMISSION_DENIED_JSON = toJsonString(Map.of(
            "code", "permission_denied", "message", "您没有权限执行该操作"));

    private static final String ADMIN_URL = "/api/admin";

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

        if (request.getRequestURI().startsWith(ADMIN_URL) && !user.isAdmin()) {
            sendPermissionDenied(response);
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

    private void sendPermissionDenied(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(PERMISSION_DENIED_JSON);
    }

}
