package com.flash.framework.operator.log.common.comsuner;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;

/**
 * @author zhurg
 * @date 2020/4/30 - 下午8:33
 */
public interface OperationLogConsumer {

    void consume(OperationLogDTO message);
}