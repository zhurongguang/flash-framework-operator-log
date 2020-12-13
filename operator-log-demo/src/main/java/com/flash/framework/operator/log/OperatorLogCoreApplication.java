package com.flash.framework.operator.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午5:28
 */
@SpringBootApplication(scanBasePackages = {"com.flash.framework.operator.log"})
public class OperatorLogCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperatorLogCoreApplication.class, args);
    }
}