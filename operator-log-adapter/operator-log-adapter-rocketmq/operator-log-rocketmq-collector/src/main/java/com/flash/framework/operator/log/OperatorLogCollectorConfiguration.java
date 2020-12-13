package com.flash.framework.operator.log;

import com.flash.framework.operator.log.collector.RocketMqOperationLogCollector;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2020/5/2 - 下午8:54
 */
@Configuration
public class OperatorLogCollectorConfiguration {

    @Bean
    @ConditionalOnMissingBean(OperationLogCollector.class)
    public OperationLogCollector operationLogCollector() {
        return new RocketMqOperationLogCollector();
    }
}