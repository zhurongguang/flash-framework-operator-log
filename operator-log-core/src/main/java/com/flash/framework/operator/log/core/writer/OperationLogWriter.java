package com.flash.framework.operator.log.core.writer;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;

/**
 * 操作日志创建接口
 *
 * @author zhurg
 * @date 2019/2/8 - 上午10:07
 */
public interface OperationLogWriter {

    /**
     * 保存操作日志
     *
     * @param operationLogDTO
     */
    void saveOperationLog(OperationLogDTO operationLogDTO);
}