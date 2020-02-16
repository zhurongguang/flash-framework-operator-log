package com.flash.framework.operator.log.server.message.rocketmq;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.server.service.OperatorLogService;
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
    private OperatorLogService operatorLogService;

    @Override
    public void onMessage(OperationLogDTO message) {
        if (log.isDebugEnabled()) {
            log.debug("[OperationLog] accept operator log message ", JSON.toJSONString(message));
        }
        operatorLogService.insertOperatorLog(message);
    }
}