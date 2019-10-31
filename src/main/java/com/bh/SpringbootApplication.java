package com.bh;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//这个配置就是让dao接口下mybatis会自动生成生成其实现类
// 并且加入ioc容器，无需注解加入容器，当然加上注解也没有错
@MapperScan("com.bh.dao")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
