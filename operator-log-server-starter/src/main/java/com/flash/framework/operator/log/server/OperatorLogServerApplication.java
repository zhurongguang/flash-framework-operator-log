package com.flash.framework.operator.log.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @author zhurg
 * @date 2019/4/15 - 下午5:05
 */
@MapperScan(basePackages = {"com.flash.framework.operator.log.server.dao"})
@SpringBootApplication
public class OperatorLogServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OperatorLogServerApplication.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}