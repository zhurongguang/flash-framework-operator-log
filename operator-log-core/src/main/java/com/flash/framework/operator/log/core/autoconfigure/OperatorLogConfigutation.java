package com.flash.framework.operator.log.core.autoconfigure;

import com.flash.framework.operator.log.api.resover.DefaultLogParameterResover;
import com.flash.framework.operator.log.api.resover.LogParameterResover;
import com.flash.framework.operator.log.core.aop.OperatorLogAspect;
import com.flash.framework.operator.log.core.writer.OperationLogWriter;
import com.flash.framework.operator.log.core.writer.kafka.KafkaOperationLogWriter;
import com.flash.framework.operator.log.core.writer.redis.RedisOperationLogWriter;
import com.flash.framework.operator.log.core.writer.rocketmq.RocketMqOperationLogWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "rocketmq")
    public OperationLogWriter rocketMqOperatorLogWriter() {
        return new RocketMqOperationLogWriter();
    }

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "kafka")
    public OperationLogWriter kafkaOperatorLogWriter() {
        return new KafkaOperationLogWriter();
    }

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "redis", matchIfMissing = true)
    public OperationLogWriter redisOperatorLogWriter() {
        return new RedisOperationLogWriter();
    }


    @Bean
    @ConditionalOnMissingBean
    public LogParameterResover logParameterResover() {
        return new DefaultLogParameterResover();
    }
}