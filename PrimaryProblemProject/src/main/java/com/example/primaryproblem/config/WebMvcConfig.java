package com.example.primaryproblem.config;

import com.example.primaryproblem.config.confbean.AtomHandlerInterceptorBean;
import com.example.primaryproblem.config.interceptor.AtomHandlerInterceptor;
import com.example.primaryproblem.webcomponents.RepeatableReadFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private AtomHandlerInterceptor atomHandlerInterceptor;

    @Autowired
    private RepeatableReadFilter repeatableReadFilter;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(atomHandlerInterceptor).addPathPatterns(AtomHandlerInterceptorBean.interceptPath);
    }


    @Bean
    public FilterRegistrationBean<RepeatableReadFilter> filterRegistrationBean() {

        final FilterRegistrationBean<RepeatableReadFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addUrlPatterns("/*");

        registrationBean.setFilter(repeatableReadFilter);

        return registrationBean;
    }
}
