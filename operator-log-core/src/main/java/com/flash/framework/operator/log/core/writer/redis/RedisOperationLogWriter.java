package com.flash.framework.operator.log.core.writer.redis;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.core.writer.OperationLogWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:11
 */
@Slf4j
public class RedisOperationLogWriter implements OperationLogWriter {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveOperationLog(OperationLogDTO operationLogDTO) {
        redisTemplate.convertAndSend(operatorLogTopic, operationLogDTO);
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
        }
    }
}
