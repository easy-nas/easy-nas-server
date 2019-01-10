package com.easynas.server.config;

import com.easynas.server.handler.LogInterceptor;
import com.easynas.server.handler.LoginCheckInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class WebAppInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录验证拦截器
        registry.addInterceptor(getLoginCheckInterceptor())
                .addPathPatterns("/**")
                .addPathPatterns("/register")
                .excludePathPatterns("/static/**");

        //请求记录日志拦截器
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }

    @Bean
    public LoginCheckInterceptor getLoginCheckInterceptor() {
        return new LoginCheckInterceptor();
    }
}
