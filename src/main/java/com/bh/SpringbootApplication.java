package com.bh;


import com.bh.Interceptor.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//这个配置就是让dao接口下mybatis会自动生成生成其实现类
// 并且加入ioc容器，无需注解加入容器，当然加上注解也没有错
@MapperScan("com.bh.dao")

public class SpringbootApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration ir=registry.addInterceptor(new LoginInterceptor());
        ir.addPathPatterns("/**");
        ir.excludePathPatterns("/login","/js/**","/html/**","/image/**","/css/**","/user/login/**");
    }



}
