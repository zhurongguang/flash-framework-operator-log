package com.flash.framework.operator.log.consumer;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.comsuner.OperationLogConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午2:32
 */
@Slf4j
@RocketMQMessageListener(topic = "${operator.log.mq.topic:OPERATOR_LOG}", consumerGroup = "${operator.log.mq.group:operator_log_group}")
public class RocketMqOperatorLogMessageListener implements RocketMQListener<OperationLogDTO> {

    @Autowired
    private OperationLogConsumer operationLogConsumer;

    @Override
    public void onMessage(OperationLogDTO message) {
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message ", JSON.toJSONString(message));
        }
        operationLogConsumer.consume(message);
    }
}