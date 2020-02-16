package com.flash.framework.operator.log.common.processor;


import com.flash.framework.operator.log.common.dto.OperationLogDTO;

/**
 * 日志生成处理器
 *
 * @author zhurg
 * @date 2019/3/19 - 下午4:49
 */
public interface OperationLogProcessor {

    /**
     * 操作执行前日志处理，可用来设置历史记录
     *
     * @param operationLogDTO 日志
     * @param args            入参
     */
    void beforeProcess(OperationLogDTO operationLogDTO, Object... args);

    /**
     * 操作执行后日志处理，可用来记录新纪录
     *
     * @param operationLogDTO 日志
     * @param args            入参
     * @param result          业务执行结果
     */
    void afterProcess(OperationLogDTO operationLogDTO, Object result, Object... args);

    /**
     * 异常处理
     *
     * @param operationLogDTO 日志
     * @param args            入参
     */
    void exceptionProcess(OperationLogDTO operationLogDTO, Object... args);
}