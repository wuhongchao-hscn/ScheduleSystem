package com.hitachi.schedule.controller.configuration;

import com.hitachi.schedule.controller.component.InterceptorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private InterceptorUtil scheduleInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("/");
        excludeList.add("/error");
        excludeList.add("/reloginlink");
        excludeList.add("/logout");
        excludeList.add("/GSAXS010.html");
        excludeList.add("/GSAXS010Login");
        excludeList.add("/GSAXS010Display");
        excludeList.add("/css/**");
        excludeList.add("/js/**");
        excludeList.add("/images/**");
        excludeList.add("/webjars/**");

        registry.addInterceptor(scheduleInterceptor).addPathPatterns("/**")
                .excludePathPatterns(excludeList);

    }
}
