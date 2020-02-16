package com.flash.framework.operator.log.core.writer.kafka;

import com.alibaba.fastjson.JSON;
import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.core.writer.OperationLogWriter;
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
public class KafkaOperationLogWriter implements OperationLogWriter {

    @Value("${operator.log.mq.topic:OPERATOR_LOG}")
    private String operatorLogTopic;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void saveOperationLog(OperationLogDTO operationLogDTO) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(operatorLogTopic, operationLogDTO);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                if (log.isDebugEnabled()) {
                    log.debug("[OperationLog] send operator log {} success", JSON.toJSONString(operationLogDTO));
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("[OperationLog] send operator log message failed ", ex);
            }
        });
    }
}