package com.easynas.server.handler;

import com.easynas.server.config.GlobalStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.easynas.server.util.JsonUtils.toJsonString;

/**
 * @author liangyongrui
 */
@Component
@Slf4j
public class LockInterceptor implements HandlerInterceptor {


    private static final String LOCK_ERROR_JSON = toJsonString(Map.of(
            "code", "lock_error", "message", "管理员正在升级系统，请稍后再试"));
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        if (GlobalStatus.isLock()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(LOCK_ERROR_JSON);
            return false;
        }

        return true;
    }

}
