package com.flash.framework.operator.log.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author zhurg
 * @date 2019/4/15 - 下午5:05
 */
@MapperScan(basePackages = {"com.flash.framework.operator.log.server.dao"})
@SpringBootApplication
public class OperatorLogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperatorLogServerApplication.class, args);
    }
}