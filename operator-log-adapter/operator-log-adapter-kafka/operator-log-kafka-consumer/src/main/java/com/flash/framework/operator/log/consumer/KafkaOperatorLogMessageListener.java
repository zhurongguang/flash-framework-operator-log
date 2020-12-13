package com.flash.framework.operator.log.consumer;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.comsuner.OperationLogConsumer;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午2:32
 */
@Slf4j
public class KafkaOperatorLogMessageListener {

    @Autowired
    private OperationLogConsumer operationLogConsumer;

    @KafkaListener(topics = "${operator.log.mq.topic:OPERATOR_LOG}", groupId = "${operator.log.mq.group:operator_log_group}")
    public void onMessage(String message) {
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message {}", message);
        }
        if (StringUtils.isNotBlank(message)) {
            OperationLogDTO operationLogDTO = JSON.parseObject(message, OperationLogDTO.class);
            operationLogConsumer.consume(operationLogDTO);
        }
    }
}