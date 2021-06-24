package com.flash.framework.operator.log.consumer;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.comsuner.OperationLogConsumer;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.Charset;

/**
 * @author zhurg
 * @date 2019/12/23 - 下午4:22
 */
@Slf4j
public class RedisOperatorLogMessageListener implements MessageListener {

    @Autowired
    private OperationLogConsumer operationLogConsumer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody(), Charset.forName("UTF-8"));
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message {}", json);
        }
        if (StringUtils.isNotBlank(json)) {
            OperationLogDTO operationLogDTO = JSON.parseObject(json, OperationLogDTO.class);
            operationLogConsumer.consume(operationLogDTO);
        }
    }
}
