package com.leyou.config;

import com.leyou.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName MvcConfig
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-20
 * @Version V1.0
 **/
@Configuration  //标记配置类
@EnableConfigurationProperties(JwtProperties.class)  //使JwtProperties生效，
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截全部    /**
        registry.addInterceptor(new UserInterceptor(jwtProperties)).addPathPatterns("/**");
    }
}
