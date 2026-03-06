package com.home.carcosa.usermanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.home.carcosa.usermanagement.security.RoleAuthorizationInterceptor;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebMvcSecurityConfiguration implements WebMvcConfigurer{

    private final RoleAuthorizationInterceptor roleAuthorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(roleAuthorizationInterceptor).order(-100);
    }
}
