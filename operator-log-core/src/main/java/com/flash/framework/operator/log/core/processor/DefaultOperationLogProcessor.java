package com.flash.framework.operator.log.core.processor;

import com.flash.framework.operator.log.common.dto.OperationLogDTO;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 默认日志处理器
 *
 * @author zhurg
 * @date 2020/4/30 - 上午11:08
 */
@Component
public class DefaultOperationLogProcessor implements OperationLogProcessor<Map<String, Object>> {

    public static final String DEFAULT_OPERATION_LOG_PROCESSOR = "DEFAULT";

    @Override
    public void beforeProcess(OperationLogDTO operationLogDTO, Object... args) {

    }

    @Override
    public void afterProcess(OperationLogDTO operationLogDTO, Object result, Object... args) {

    }

    @Override
    public void exceptionProcess(OperationLogDTO operationLogDTO, Object... args) {

    }

    @Override
    public Set<String> forOperations() {
        return Sets.newHashSet(DEFAULT_OPERATION_LOG_PROCESSOR);
    }
}
