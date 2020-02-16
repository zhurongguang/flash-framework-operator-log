package com.flash.framework.operator.log.server.message.kafka;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.service.OperatorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午2:32
 */
@Slf4j
public class KafkaOperatorLogMessageListener {

    @Autowired
    private OperatorLogService operatorLogService;

    @KafkaListener(topics = "${operator.log.mq.topic:OPERATOR_LOG}", groupId = "${operator.log.mq.group:operator_log_group}")
    public void onMessage(OperationLogDTO operationLog) {
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message ", JSON.toJSONString(operationLog));
        }
        operatorLogService.insertOperatorLog(operationLog);
    }
}