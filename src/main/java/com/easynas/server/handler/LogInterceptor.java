package com.easynas.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liangyongrui
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        try {
            String requestLogId = request.hashCode() + "" + System.currentTimeMillis();
            request.setAttribute("request_logId", requestLogId);
            log.info("Request: uri: {}, request_logId: {}, session_id:{}, ip: {}, " +
                            "method: {}, content_type:{}, queryString: {}",
                    request.getRequestURI(), requestLogId,
                    request.getRequestedSessionId(), getRequestIP(request),
                    request.getMethod(), request.getContentType(),
                    request.getQueryString());
        } catch (Exception e) {
            log.error("RequestLogInterceptor:preHandle: ", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return;
        }
        try {
            String requestLogId = String.valueOf(request.getAttribute("request_logId"));
            if (modelAndView != null) {
                log.info("Response: request_logId: {}, reponseStatus: {}, modelAndView: {}", requestLogId, response.getStatus(),
                        modelAndView.getViewName());
                return;
            }
            log.info("Response: request_logId: {}, reponseStatus: {}", requestLogId, response.getStatus());
        } catch (Exception e) {
            log.error("RequestLogInterceptor:postHandle: ", e);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {

    }

    private String getRequestIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
