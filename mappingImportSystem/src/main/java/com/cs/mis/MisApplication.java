package com.cs.mis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author wcy
 */
@SpringBootApplication
@MapperScan("com.cs.mis.mapper")
public class MisApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(MisApplication.class,args);
    }
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
