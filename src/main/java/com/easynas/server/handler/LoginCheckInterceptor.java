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
import java.util.Optional;

/**
 * @author liangyongrui
 * @date 19-1-9 下午8:55
 */
@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("access-token");
        if (header == null) {
            // Result.fail("登录验证失败请重新登录");
            return false;
        }
        User user = LoginSession.getUserByToken(header);
        if (user == null) {
            // Result.fail("登录验证失败请重新登录");
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
}
