package com.flash.framework.operator.log.server.message.redis;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.service.OperatorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:22
 */
@Slf4j
public class RedisOperatorLogMessageListener implements MessageListener {

    @Autowired
    private OperatorLogService operatorLogService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer redisSerializer = redisTemplate.getValueSerializer();
        OperationLogDTO operationLogDTO = (OperationLogDTO) redisSerializer.deserialize(message.getBody());
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message ", JSON.toJSONString(operationLogDTO));
        }
        operatorLogService.insertOperatorLog(operationLogDTO);
    }
}
