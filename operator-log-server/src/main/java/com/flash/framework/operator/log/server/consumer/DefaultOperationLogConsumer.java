package com.flash.framework.operator.log.server.consumer;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.flash.framework.operator.log.common.comsuner.OperationLogConsumer;
import com.flash.framework.operator.log.server.service.OperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhurg
 * @date 2020/5/4 - 下午9:09
 */
@Component
public class DefaultOperationLogConsumer implements OperationLogConsumer {

    @Autowired
    private OperatorLogService operatorLogService;

    @Override
    public void consume(OperationLogDTO message) {
        operatorLogService.insert(message);
    }
}
