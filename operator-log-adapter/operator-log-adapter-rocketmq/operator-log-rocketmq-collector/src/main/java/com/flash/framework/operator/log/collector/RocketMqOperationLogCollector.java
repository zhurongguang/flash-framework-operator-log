package com.flash.framework.operator.log.collector;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * RocketMQ日志收集实现
 *
 * @author zhurg
 * @date 2019/4/15 - 上午10:23
 */
@Slf4j
public class RocketMqOperationLogCollector implements OperationLogCollector {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void collectOperationLog(OperationLogDTO operationLogDTO) {
        rocketMQTemplate.asyncSend(operatorLogTopic, operationLogDTO, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (log.isDebugEnabled()) {
                    log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
                }
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("[OperationLog] send operator log message failed,cause:{}", Throwables.getStackTraceAsString(throwable));
            }
        });
    }
}