package com.flash.framework.operator.log;

import com.flash.framework.operator.log.consumer.RedisOperatorLogMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author zhurg
 * @date 2020/5/2 - 下午8:58
 */
@Configuration
public class OperatorLogConsumerConfiguration {

    @Bean
    public RedisOperatorLogMessageListener redisOperatorLogMessageListener() {
        return new RedisOperatorLogMessageListener();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       RedisOperatorLogMessageListener listenerAdapter, @Value("${operator.log.mq.topic:OPERATOR_LOG}") String topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new MessageListenerAdapter(listenerAdapter), new PatternTopic(topic));
        return container;
    }
}