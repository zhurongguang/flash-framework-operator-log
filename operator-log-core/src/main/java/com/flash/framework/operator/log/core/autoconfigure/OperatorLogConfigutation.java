package com.flash.framework.operator.log.core.autoconfigure;

import com.flash.framework.operator.log.common.resover.LogParameterResover;
import com.flash.framework.operator.log.core.aop.OperatorLogAspect;
import com.flash.framework.operator.log.core.processor.builder.DefaultOperationBuilder;
import com.flash.framework.operator.log.core.processor.builder.OperationBuilder;
import com.flash.framework.operator.log.core.resover.DefaultLogParameterResover;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

/**
 * @author zhurg
 * @date 2019/4/15 - 上午10:34
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.flash.framework.operator.log.core"})
public class OperatorLogConfigutation {

    @Bean
    @Order
    public OperatorLogAspect operatorLogAspect() {
        return new OperatorLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder() {
        return new DefaultOperationBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public LogParameterResover logParameterResover() {
        return new DefaultLogParameterResover();
    }
}