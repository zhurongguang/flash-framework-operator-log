package com.flash.framework.operator.log.collector;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import java.nio.charset.Charset;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:11
 */
@Slf4j
public class RedisOperationLogCollector implements OperationLogCollector {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Value("${operator.log.redis.database:6}")
    private int operatorLogDatabase;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void collectOperationLog(OperationLogDTO operationLogDTO) {
        RedisConnection conn = null;
        try {
            conn = RedisConnectionUtils.getConnection(redisConnectionFactory);

            conn.select(operatorLogDatabase);
            
            conn.publish(operatorLogTopic.getBytes(Charset.forName("UTF-8")), JSON.toJSONString(operationLogDTO).getBytes(Charset.forName("UTF-8")));
            if (log.isDebugEnabled()) {
                log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, redisConnectionFactory, false);
        }
    }
}
