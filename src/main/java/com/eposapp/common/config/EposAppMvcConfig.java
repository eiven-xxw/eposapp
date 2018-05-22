package com.eposapp.common.config;

import com.eposapp.common.constant.SysConstants;
import com.eposapp.interceptor.UserLoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class EposAppMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private UserLoginHandlerInterceptor userLoginHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginHandlerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/doLogin","/test");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("*").allowedHeaders("*")
                .exposedHeaders(SysConstants.RESPONSE_CODE, SysConstants.USER_ACCESS_TOKEN);

    }
}
