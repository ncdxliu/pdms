package com.dnliu.pdms.config;

import com.dnliu.pdms.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dnliu
 * @date 2021-09-13 21:18
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private RequestInterceptor requestInterceptor;

    @Autowired
    WebConfig(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器，配置拦截地址
        registry.addInterceptor(requestInterceptor).addPathPatterns("/pdms/**");
    }
}
