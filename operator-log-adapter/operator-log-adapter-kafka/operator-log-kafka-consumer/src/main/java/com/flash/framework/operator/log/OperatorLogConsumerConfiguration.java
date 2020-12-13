package com.flash.framework.operator.log;

import com.flash.framework.operator.log.consumer.KafkaOperatorLogMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2020/5/2 - 下午8:58
 */
@Configuration
public class OperatorLogConsumerConfiguration {

    @Bean
    public KafkaOperatorLogMessageListener operatorLogMessageListener() {
        return new KafkaOperatorLogMessageListener();
    }
}