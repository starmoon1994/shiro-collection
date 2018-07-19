package com.company.testshiro4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
// mapper 接口类扫描包配置
@MapperScan("com.company.testshiro4.mapper")
@ImportResource(locations = {"classpath:spring-*.xml"})
public class Testshiro4Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Testshiro4Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Testshiro4Application.class);
    }
}
