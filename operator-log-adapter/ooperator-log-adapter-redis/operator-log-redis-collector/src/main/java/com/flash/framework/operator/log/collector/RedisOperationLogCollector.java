package com.flash.framework.operator.log.collector;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:11
 */
@Slf4j
public class RedisOperationLogCollector implements OperationLogCollector {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void collectOperationLog(OperationLogDTO operationLogDTO) {
        redisTemplate.convertAndSend(operatorLogTopic, JSON.toJSONString(operationLogDTO));
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
        }
    }
}
