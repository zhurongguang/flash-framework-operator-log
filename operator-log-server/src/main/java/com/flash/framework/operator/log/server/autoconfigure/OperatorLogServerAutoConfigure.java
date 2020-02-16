package com.flash.framework.operator.log.server.autoconfigure;

import com.flash.framework.operator.log.server.message.kafka.KafkaOperatorLogMessageListener;
import com.flash.framework.operator.log.server.message.redis.RedisOperatorLogMessageListener;
import com.flash.framework.operator.log.server.message.rocketmq.RocketMqOperatorLogMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午4:35
 */
@Configuration
public class OperatorLogServerAutoConfigure {

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "rocketmq")
    public RocketMqOperatorLogMessageListener rocketMqOperatorLogMessageListener() {
        return new RocketMqOperatorLogMessageListener();
    }

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "kafka")
    public KafkaOperatorLogMessageListener kafkaOperatorLogMessageListener() {
        return new KafkaOperatorLogMessageListener();
    }

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "redis", matchIfMissing = true)
    public RedisOperatorLogMessageListener redisOperatorLogMessageListener() {
        return new RedisOperatorLogMessageListener();
    }

    @Bean
    @ConditionalOnProperty(name = "operator.log.mq.mode", havingValue = "redis", matchIfMissing = true)
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   RedisOperatorLogMessageListener listenerAdapter, @Value("${operator.log.mq.topic:OPERATOR_LOG}") String topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new MessageListenerAdapter(listenerAdapter), new PatternTopic(topic));
        return container;
    }
}