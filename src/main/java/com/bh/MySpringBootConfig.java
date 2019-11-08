package com.bh;

import com.bh.interceptors.AdminAuthroityInterceptor;
import com.bh.interceptors.PortalAuthorityInterceptor;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/*
*配置拦截器的类
* */

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer {
    //拦截后台请求，验证用户是否登录
    @Autowired
    AdminAuthroityInterceptor adminAuthroityInterceptor;
    @Autowired
    PortalAuthorityInterceptor portalAuthorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthroityInterceptor)
                .addPathPatterns("/manage/**")
                .excludePathPatterns("/manage/login/**");


//        user
//        定义一个list
        List<String> addPatterns = Lists.newArrayList();
        addPatterns.add("/user/**");
        addPatterns.add("/cart/**");
        addPatterns.add("/order/**");
        addPatterns.add("/shipping/**");

        List<String> excludePathPatterns = Lists.newArrayList();
        excludePathPatterns.add("/user/register.do");
        excludePathPatterns.add("/user/login/**");
        excludePathPatterns.add("/user/forget_get_question/**");
        excludePathPatterns.add("/user/forget_check_answer.do");
        excludePathPatterns.add("/user/forget_reset_password.do");
        excludePathPatterns.add("/user/update_information.do");
        excludePathPatterns.add("/order/callback.do");
        excludePathPatterns.add("/order/callback.do");
        //开放购物车板块，vue调试要用
        excludePathPatterns.add("/cart/");

        registry.addInterceptor(portalAuthorityInterceptor)
                .addPathPatterns(addPatterns)
                .excludePathPatterns(excludePathPatterns);
    }
}
