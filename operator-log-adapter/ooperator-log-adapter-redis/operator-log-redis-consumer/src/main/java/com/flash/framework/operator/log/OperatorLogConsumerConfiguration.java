package com.flash.framework.operator.log;

import com.flash.framework.operator.log.consumer.RedisOperatorLogMessageListener;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.time.Duration;
import java.util.Objects;

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
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisProperties redisProperties, RedisConnectionFactory connectionFactory,
                                                                       RedisOperatorLogMessageListener listenerAdapter,
                                                                       @Value("${operator.log.mq.topic:OPERATOR_LOG}") String topic,
                                                                       @Value("${operator.log.redis.database:6}") Integer database) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        RedisConnectionFactory conn = null;
        if (connectionFactory instanceof JedisConnectionFactory) {
            JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) connectionFactory;
            if (Objects.nonNull(jedisConnectionFactory.getSentinelConfiguration())) {
                RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
                redisSentinelConfiguration.setDatabase(database);
                redisSentinelConfiguration.setMaster(jedisConnectionFactory.getSentinelConfiguration().getMaster());
                redisSentinelConfiguration.setPassword(jedisConnectionFactory.getSentinelConfiguration().getPassword());
                redisSentinelConfiguration.setSentinelPassword(jedisConnectionFactory.getSentinelConfiguration().getSentinelPassword());
                redisSentinelConfiguration.setSentinels(jedisConnectionFactory.getSentinelConfiguration().getSentinels());
                conn = new JedisConnectionFactory(redisSentinelConfiguration, JedisClientConfiguration.builder()
                        .connectTimeout(Objects.nonNull(redisProperties.getTimeout()) ? redisProperties.getTimeout() : Duration.ofMillis(10000))
                        .usePooling()
                        .poolConfig(new GenericObjectPoolConfig())
                        .build());
            } else if (Objects.nonNull(jedisConnectionFactory.getClusterConfiguration())) {
                RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
                redisClusterConfiguration.setClusterNodes(jedisConnectionFactory.getClusterConfiguration().getClusterNodes());
                redisClusterConfiguration.setMaxRedirects(jedisConnectionFactory.getClusterConfiguration().getMaxRedirects());
                redisClusterConfiguration.setPassword(jedisConnectionFactory.getClusterConfiguration().getPassword());
                conn = new JedisConnectionFactory(redisClusterConfiguration, JedisClientConfiguration.builder()
                        .connectTimeout(Objects.nonNull(redisProperties.getTimeout()) ? redisProperties.getTimeout() : Duration.ofMillis(10000))
                        .usePooling()
                        .poolConfig(new GenericObjectPoolConfig())
                        .build());
            } else {
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(redisProperties.getHost());
                redisStandaloneConfiguration.setPort(redisProperties.getPort());
                redisStandaloneConfiguration.setDatabase(database);
                redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
                conn = new JedisConnectionFactory(redisStandaloneConfiguration, JedisClientConfiguration.builder()
                        .connectTimeout(Objects.nonNull(redisProperties.getTimeout()) ? redisProperties.getTimeout() : Duration.ofMillis(10000))
                        .usePooling()
                        .poolConfig(new GenericObjectPoolConfig())
                        .build());
            }
        } else {
            LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) connectionFactory;
            if (Objects.nonNull(lettuceConnectionFactory.getSentinelConfiguration())) {
                RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
                redisSentinelConfiguration.setDatabase(database);
                redisSentinelConfiguration.setMaster(lettuceConnectionFactory.getSentinelConfiguration().getMaster());
                redisSentinelConfiguration.setPassword(lettuceConnectionFactory.getSentinelConfiguration().getPassword());
                redisSentinelConfiguration.setSentinelPassword(lettuceConnectionFactory.getSentinelConfiguration().getSentinelPassword());
                redisSentinelConfiguration.setSentinels(lettuceConnectionFactory.getSentinelConfiguration().getSentinels());
                conn = new LettuceConnectionFactory(redisSentinelConfiguration);
            } else if (Objects.nonNull(lettuceConnectionFactory.getClusterConfiguration())) {
                RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
                redisClusterConfiguration.setClusterNodes(lettuceConnectionFactory.getClusterConfiguration().getClusterNodes());
                redisClusterConfiguration.setMaxRedirects(lettuceConnectionFactory.getClusterConfiguration().getMaxRedirects());
                redisClusterConfiguration.setPassword(lettuceConnectionFactory.getClusterConfiguration().getPassword());
                conn = new JedisConnectionFactory(redisClusterConfiguration);
            } else {
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(redisProperties.getHost());
                redisStandaloneConfiguration.setPort(redisProperties.getPort());
                redisStandaloneConfiguration.setDatabase(database);
                redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
                conn = new JedisConnectionFactory(redisStandaloneConfiguration);
            }
        }
        container.setConnectionFactory(conn);
        container.addMessageListener(new MessageListenerAdapter(listenerAdapter), new PatternTopic(topic));
        return container;
    }
}