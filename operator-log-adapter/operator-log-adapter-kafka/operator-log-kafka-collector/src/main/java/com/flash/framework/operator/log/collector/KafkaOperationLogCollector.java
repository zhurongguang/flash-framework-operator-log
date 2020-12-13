package com.flash.framework.operator.log.collector;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.collector.OperationLogCollector;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午4:11
 */
@Slf4j
public class KafkaOperationLogCollector implements OperationLogCollector {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void collectOperationLog(OperationLogDTO operationLogDTO) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(operatorLogTopic, JSON.toJSONString(operationLogDTO));

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                if (log.isDebugEnabled()) {
                    log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("[OperationLog] send operator log message failed,cause:{}", Throwables.getStackTraceAsString(ex));
            }
        });
    }
}