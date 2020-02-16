package com.flash.framework.operator.log.api;

/**
 * @author zhurg
 * @date 2019/9/12 - 上午10:57
 */
public interface OperationLogConstants {

    String OPERATION_LOG_PROCESSOR = "logProcessor";

    /**
     * 标识符
     */
    String POINT = ".";

    /**
     * 操作日志：操作成功
     */
    Integer OPERATOR_LOG_SUCCESS = 0;

    /**
     * 操作日志：操作失败
     */
    Integer OPERATOR_LOG_ERROR = 1;
}